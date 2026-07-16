package com.regulareedge.authservice.mapper;

import com.regulareedge.authservice.common.enums.UserStatus;
import com.regulareedge.authservice.dto.request.RegisterRequest;
import com.regulareedge.authservice.dto.response.UserResponse;
import com.regulareedge.authservice.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setBusinessUnitId(request.getBusinessUnitId());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getBusinessUnitId(),
                user.getRole(),
                user.getStatus());
    }
}
