package com.example.shinra.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.shinra.payload.request.JoinRoomRequest;
import com.example.shinra.payload.request.MessageRequest;
import com.example.shinra.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SocketController {

    private final SocketIOServer socketIOServer;

    private final SocketService socketService;


    @PostConstruct
    public void setSocketIOMapping() {

        socketIOServer.addConnectListener(socketService::connect);
        socketIOServer.addDisconnectListener(socketService::disconnect);

        socketIOServer.addEventListener("joinRoom", JoinRoomRequest.class,
                ((client, room, ackSender) -> socketService.joinRoom(client, room)));

        socketIOServer.addEventListener("send", MessageRequest.class,
                ((client, data, ackSender) -> socketService.sendMessage(client, data)));
    }
}
