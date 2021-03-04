package com.example.shinra.service.user;

import com.example.shinra.entity.user.User;
import com.example.shinra.entity.user.UserRepository;
import com.example.shinra.payload.response.UserListResponse;
import com.example.shinra.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserListResponse getUserList(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponse> userResponses = new ArrayList<>();

        for(User user : users) {
            userResponses.add(
                    UserResponse.builder()
                            .name(user.getName())
                            .userEmail(user.getEmail())
                            .build()
            );
        }

        return UserListResponse.builder()
                .totalElements(users.getTotalElements())
                .totalPages(users.getTotalPages())
                .userResponses(userResponses)
                .build();
    }
}
