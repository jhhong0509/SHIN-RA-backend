package com.example.shinra.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccessTokenResponse {

    @JsonProperty("access-token")
    private String accessToken;

}
