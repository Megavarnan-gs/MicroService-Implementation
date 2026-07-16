package com.regulareedge.authservice.service.interfaces;

import com.regulareedge.authservice.common.enums.UserRole;
import com.regulareedge.authservice.common.enums.UserStatus;
import com.regulareedge.authservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponse> getAllUsers(Pageable pageable);

    Page<UserResponse> getUsersByRole(UserRole role, Pageable pageable);

    UserResponse updateStatus(String email, UserStatus status);

    void deleteByEmail(String email);
}
