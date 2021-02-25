package com.example.shinra.service.auth;

import com.example.shinra.payload.request.SignInRequest;
import com.example.shinra.payload.request.SignUpRequest;
import com.example.shinra.payload.response.AccessTokenResponse;
import com.example.shinra.payload.response.TokenResponse;

public interface AuthService {
    AccessTokenResponse tokenRefresh(String receivedToken);

    TokenResponse signIn(SignInRequest request);

    void signUp(SignUpRequest request);
}
