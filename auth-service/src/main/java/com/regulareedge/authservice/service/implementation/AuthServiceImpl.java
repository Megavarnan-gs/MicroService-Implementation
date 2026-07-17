package com.regulareedge.authservice.service.implementation;

import com.regulareedge.authservice.config.JwtProperties;
import com.regulareedge.authservice.dto.request.LoginRequest;
import com.regulareedge.authservice.dto.request.RegisterRequest;
import com.regulareedge.authservice.dto.response.LoginResponse;
import com.regulareedge.authservice.dto.response.TokenResponse;
import com.regulareedge.authservice.dto.response.UserResponse;
import com.regulareedge.authservice.entity.RefreshToken;
import com.regulareedge.authservice.entity.User;
import com.regulareedge.authservice.exception.DuplicateResourceException;
import com.regulareedge.authservice.exception.InvalidCredentialsException;
import com.regulareedge.authservice.exception.TokenExpiredException;
import com.regulareedge.authservice.mapper.UserMapper;
import com.regulareedge.authservice.repository.UserRepository;
import com.regulareedge.authservice.security.JwtUtil;
import com.regulareedge.authservice.service.interfaces.AuditLogService;
import com.regulareedge.authservice.service.interfaces.AuthService;
import com.regulareedge.authservice.service.interfaces.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    public AuthServiceImpl(UserRepository userRepository,
                            AuditLogService auditLogService,
                            PasswordEncoder passwordEncoder,
                            JwtUtil jwtUtil,
                            TokenService tokenService,
                            JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A user with email " + request.getEmail() + " already exists");
        }

        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        auditLogService.log(savedUser.getUserId(), "REGISTER", "User", savedUser.getUserId());

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            auditLogService.log(null, "LOGIN_FAILED", "User", null);
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            auditLogService.log(user.getUserId(), "LOGIN_FAILED", "User", user.getUserId());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole().name());

        tokenService.createRefreshToken(user.getUserId(), refreshToken);

        auditLogService.log(user.getUserId(), "LOGIN_SUCCESS", "User", user.getUserId());

        return new LoginResponse(accessToken, refreshToken,
                jwtProperties.getAccessTokenExpirationMs() / 1000, user.getRole().name());
    }

    @Override
    public TokenResponse refresh(String refreshToken) {
        tokenService.verifyRefreshToken(refreshToken);

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new TokenExpiredException("Refresh token is invalid or expired");
        }

        String email = jwtUtil.extractEmail(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(email, role);

        return new TokenResponse(newAccessToken, refreshToken, jwtProperties.getAccessTokenExpirationMs() / 1000);
    }
}
