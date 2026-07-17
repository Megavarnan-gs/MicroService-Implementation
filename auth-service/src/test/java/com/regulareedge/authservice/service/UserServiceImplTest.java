package com.regulareedge.authservice.service;

import com.regulareedge.authservice.common.enums.UserRole;
import com.regulareedge.authservice.common.enums.UserStatus;
import com.regulareedge.authservice.dto.response.UserResponse;
import com.regulareedge.authservice.entity.User;
import com.regulareedge.authservice.exception.ResourceNotFoundException;
import com.regulareedge.authservice.repository.UserRepository;
import com.regulareedge.authservice.service.implementation.UserServiceImpl;
import com.regulareedge.authservice.service.interfaces.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogService auditLogService;

    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, auditLogService);

        user = new User();
        user.setUserId(1);
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.CO);
        user.setStatus(UserStatus.ACTIVE);
    }

    @Test
    void getAllUsers_shouldReturnPagedUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponse> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("jane.doe@example.com", result.getContent().get(0).getEmail());
    }

    @Test
    void getUsersByRole_shouldReturnPagedUsersForRole() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

        when(userRepository.findByRole(UserRole.CO, pageable)).thenReturn(userPage);

        Page<UserResponse> result = userService.getUsersByRole(UserRole.CO, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(UserRole.CO, result.getContent().get(0).getRole());
    }

    @Test
    void updateStatus_shouldUpdateAndReturnUser_whenUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.updateStatus(user.getEmail(), UserStatus.INACTIVE);

        assertEquals(user.getEmail(), response.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void updateStatus_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateStatus("missing@example.com", UserStatus.INACTIVE));
    }

    @Test
    void deleteByEmail_shouldDeleteUser_whenUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.deleteByEmail(user.getEmail());

        verify(userRepository).delete(user);
    }

    @Test
    void deleteByEmail_shouldThrowResourceNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteByEmail("missing@example.com"));
    }
}
