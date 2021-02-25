package com.example.shinra.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.shinra.payload.request.JoinRoomRequest;
import com.example.shinra.payload.request.MessageRequest;
import org.springframework.web.socket.WebSocketSession;

public interface SocketService {
    void connect(SocketIOClient client);

    void disconnect(SocketIOClient client);

    void joinRoom(SocketIOClient client, JoinRoomRequest room);

    void sendMessage(SocketIOClient client, MessageRequest request);

}
