package com.regulareedge.authservice.service;

import com.regulareedge.authservice.common.enums.UserRole;
import com.regulareedge.authservice.config.JwtProperties;
import com.regulareedge.authservice.dto.request.LoginRequest;
import com.regulareedge.authservice.dto.request.RegisterRequest;
import com.regulareedge.authservice.dto.response.LoginResponse;
import com.regulareedge.authservice.dto.response.UserResponse;
import com.regulareedge.authservice.entity.User;
import com.regulareedge.authservice.exception.DuplicateResourceException;
import com.regulareedge.authservice.exception.InvalidCredentialsException;
import com.regulareedge.authservice.repository.UserRepository;
import com.regulareedge.authservice.security.JwtUtil;
import com.regulareedge.authservice.service.implementation.AuthServiceImpl;
import com.regulareedge.authservice.service.interfaces.AuditLogService;
import com.regulareedge.authservice.service.interfaces.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private JwtProperties jwtProperties;

    private RegisterRequest registerRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("RegulareEdgeSecretKeyForJwtSigningMustBeAtLeast256BitsLongForHS256Algorithm2026");
        jwtProperties.setAccessTokenExpirationMs(3600000);
        jwtProperties.setRefreshTokenExpirationMs(604800000);

        authService = new AuthServiceImpl(userRepository, auditLogService, passwordEncoder, jwtUtil,
                tokenService, jwtProperties);

        registerRequest = new RegisterRequest();
        registerRequest.setName("Jane Doe");
        registerRequest.setEmail("jane.doe@example.com");
        registerRequest.setPassword("Password123");
        registerRequest.setPhone("1234567890");
        registerRequest.setBusinessUnitId(1);
        registerRequest.setRole(UserRole.CO);

        existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setName("Jane Doe");
        existingUser.setEmail("jane.doe@example.com");
        existingUser.setPassword("encodedPassword");
        existingUser.setRole(UserRole.CO);
    }

    @Test
    void register_shouldSucceed_whenEmailIsNotTaken() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponse response = authService.register(registerRequest);

        assertEquals("jane.doe@example.com", response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowDuplicateResourceException_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(registerRequest));
    }

    @Test
    void login_shouldSucceed_whenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane.doe@example.com");
        loginRequest.setPassword("Password123");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())).thenReturn(true);
        when(jwtUtil.generateAccessToken(anyString(), anyString())).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyString(), anyString())).thenReturn("refresh-token");

        LoginResponse response = authService.login(loginRequest);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("CO", response.getRole());
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordIsWrong() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("jane.doe@example.com");
        loginRequest.setPassword("WrongPassword");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("Password123");

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }
}
