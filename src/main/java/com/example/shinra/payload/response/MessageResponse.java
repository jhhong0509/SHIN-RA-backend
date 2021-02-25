package com.example.shinra.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private LocalDateTime createdAt;

    private String message;

    private String sender;

    private boolean isDeleted;

    private boolean isShow;

}
