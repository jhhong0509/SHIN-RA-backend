package com.example.shinra.service.user;

import com.example.shinra.entity.user.User;
import com.example.shinra.payload.response.UserListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserListResponse getUserList(Pageable pageable);
}
