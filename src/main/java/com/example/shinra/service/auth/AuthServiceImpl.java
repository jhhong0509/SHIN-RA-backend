package com.example.shinra.service.auth;

import com.example.shinra.entity.refreshtoken.RefreshToken;
import com.example.shinra.entity.refreshtoken.RefreshTokenRepository;
import com.example.shinra.entity.user.User;
import com.example.shinra.entity.user.UserRepository;
import com.example.shinra.exceptions.InvalidTokenException;
import com.example.shinra.exceptions.UserAlreadyExistsException;
import com.example.shinra.payload.request.SignInRequest;
import com.example.shinra.payload.request.SignUpRequest;
import com.example.shinra.payload.response.AccessTokenResponse;
import com.example.shinra.payload.response.TokenResponse;
import com.example.shinra.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public AccessTokenResponse tokenRefresh(String receivedToken) {
        if(!jwtTokenProvider.isRefreshToken(receivedToken)) {
            throw new InvalidTokenException();
        }

        return refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(refreshToken -> refreshToken.update(refreshExp))
                .map(refreshTokenRepository::save)
                .map(refreshToken ->
                        new AccessTokenResponse(jwtTokenProvider.generateAccessToken(jwtTokenProvider.getEmail(receivedToken))))
                .orElseThrow(InvalidTokenException::new);
    }

    @Override
    public TokenResponse signIn(SignInRequest request) {
        return userRepository.findById(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(User::getEmail)
                .map(email -> {
                    String refreshToken = jwtTokenProvider.generateRefreshToken(email);
                    return new RefreshToken(email, refreshToken, refreshExp);
                })
                .map(refreshTokenRepository::save)
                .map(refreshToken -> {
                    String accessToken = jwtTokenProvider.generateAccessToken(refreshToken.getEmail());
                    return new TokenResponse(accessToken, refreshToken.getRefreshToken(), refreshExp);
                })
                .orElseThrow(InvalidTokenException::new);
    }

    @Override
    public void signUp(SignUpRequest request) {
        userRepository.findById(request.getEmail())
                .ifPresent(user -> {throw new UserAlreadyExistsException();});

        if(!request.isValidAddress()) {
            throw new UserAlreadyExistsException();
        }

        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }
}
