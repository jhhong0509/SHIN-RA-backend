package com.example.shinra.payload.request;

import com.example.shinra.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String email;

    private String password;

    private String name;

    public boolean isValidAddress() {
        String validAddress = "dsm.hs.kr";
        String emailAddress = this.email.substring(email.indexOf("@") + 1);
        return validAddress.equals(emailAddress);
    }

    public User toEntity(String password) {
        return User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
