package com.example.shinra.service.socket;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.shinra.entity.message.Message;
import com.example.shinra.entity.message.MessageRepository;
import com.example.shinra.entity.user.User;
import com.example.shinra.entity.user.UserRepository;
import com.example.shinra.exceptions.InvalidTokenException;
import com.example.shinra.exceptions.UserNotFoundException;
import com.example.shinra.exceptions.UserNotMemberException;
import com.example.shinra.payload.request.JoinRoomRequest;
import com.example.shinra.payload.request.MessageRequest;
import com.example.shinra.payload.response.MessageResponse;
import com.example.shinra.security.JwtTokenProvider;
import com.example.shinra.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final MessageRepository messageRepository;
    private final SocketIOServer socketIOServer;
    private final AuthenticationFacade authenticationFacade;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void connect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if(!jwtTokenProvider.validateToken(token)) {
            clientDisconnect(client, "invalidToken");
            throw new InvalidTokenException();
        }

        User user = userRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);
        client.set("user", user);

    }

    @Override
    public void disconnect(SocketIOClient client) {
        System.out.printf("Socket Disconnected Session: %s%n",client.getSessionId());
    }

    @Override
    public void joinRoom(SocketIOClient client, JoinRoomRequest room) {
        User user = client.get("user");
        try {
            Arrays.stream(room.getRoom().split(":")).filter(member -> member.equals(user.getEmail()));
        } catch (Exception e){
            throw new UserNotMemberException();
        }
    }

    @Override
    public void sendMessage(SocketIOClient client, MessageRequest request) {
        User user = client.get("user");
        boolean isMine = false;
        for(String name : request.getRoom().split(":")) {
            System.out.println("이름: "+ name);
            if(name.equals(user.getEmail())) {
                isMine = true;
            }
        }
        if(isMine || !client.getAllRooms().contains(request.getRoom())) {
            throw new UserNotMemberException();
        }

        Message message = messageRepository.save(request.toEntity(client));

        socketIOServer.getRoomOperations(request.getRoom()).sendEvent("receive", MessageResponse.builder()
                .createdAt(message.getCreatedDate())
                .isDeleted(message.isDeleted())
                .isShow(message.isShow())
                .sender(message.getSender())
                .message(message.getMessage())
                .build());

    }

    public void clientDisconnect(SocketIOClient client, String reason) {
        client.sendEvent("error" + reason);
        client.disconnect();
    }
}
