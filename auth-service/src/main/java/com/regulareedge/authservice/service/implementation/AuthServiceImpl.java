package com.regulareedge.authservice.service.implementation;

import com.regulareedge.authservice.config.JwtProperties;
import com.regulareedge.authservice.dto.request.LoginRequest;
import com.regulareedge.authservice.dto.request.RegisterRequest;
import com.regulareedge.authservice.dto.response.LoginResponse;
import com.regulareedge.authservice.dto.response.TokenResponse;
import com.regulareedge.authservice.dto.response.UserResponse;
import com.regulareedge.authservice.entity.AuditLog;
import com.regulareedge.authservice.entity.RefreshToken;
import com.regulareedge.authservice.entity.User;
import com.regulareedge.authservice.exception.DuplicateResourceException;
import com.regulareedge.authservice.exception.InvalidCredentialsException;
import com.regulareedge.authservice.exception.TokenExpiredException;
import com.regulareedge.authservice.mapper.UserMapper;
import com.regulareedge.authservice.repository.AuditLogRepository;
import com.regulareedge.authservice.repository.UserRepository;
import com.regulareedge.authservice.security.JwtUtil;
import com.regulareedge.authservice.service.interfaces.AuthService;
import com.regulareedge.authservice.service.interfaces.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    public AuthServiceImpl(UserRepository userRepository,
                            AuditLogRepository auditLogRepository,
                            PasswordEncoder passwordEncoder,
                            JwtUtil jwtUtil,
                            TokenService tokenService,
                            JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
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

        auditLogRepository.save(new AuditLog(savedUser.getEmail(), "REGISTER",
                "New user registered with role " + savedUser.getRole(), LocalDateTime.now()));

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getRole().name());

        tokenService.createRefreshToken(user.getUserId(), refreshToken);

        auditLogRepository.save(new AuditLog(user.getEmail(), "LOGIN", "User logged in", LocalDateTime.now()));

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
