package com.example.shinra.payload.request;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.shinra.entity.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    private String message;

    private String room;

    public Message toEntity(SocketIOClient client) {
        return Message.builder()
                .isDeleted(false)
                .isShow(false)
                .room(room)
                .sender(client.get("user"))
                .message(message)
                .build();
    }
}
