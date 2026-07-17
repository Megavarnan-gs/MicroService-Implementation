package com.regulareedge.authservice.service.implementation;

import com.regulareedge.authservice.common.enums.UserRole;
import com.regulareedge.authservice.common.enums.UserStatus;
import com.regulareedge.authservice.dto.response.UserResponse;
import com.regulareedge.authservice.entity.User;
import com.regulareedge.authservice.exception.ResourceNotFoundException;
import com.regulareedge.authservice.mapper.UserMapper;
import com.regulareedge.authservice.repository.UserRepository;
import com.regulareedge.authservice.service.interfaces.AuditLogService;
import com.regulareedge.authservice.service.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public UserServiceImpl(UserRepository userRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toResponse);
    }

    @Override
    public Page<UserResponse> getUsersByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable).map(UserMapper::toResponse);
    }

    @Override
    public UserResponse updateStatus(String email, UserStatus status) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        user.setStatus(status);
        User updatedUser = userRepository.save(user);

        auditLogService.log(updatedUser.getUserId(), "USER_STATUS_UPDATED", "User", updatedUser.getUserId());

        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        userRepository.delete(user);

        auditLogService.log(user.getUserId(), "USER_DELETED", "User", user.getUserId());
    }
}
