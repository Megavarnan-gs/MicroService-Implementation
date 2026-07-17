package com.regulareedge.authservice.service.implementation;

import com.regulareedge.authservice.dto.response.MessageResponse;
import com.regulareedge.authservice.entity.PasswordResetToken;
import com.regulareedge.authservice.entity.User;
import com.regulareedge.authservice.exception.ResourceNotFoundException;
import com.regulareedge.authservice.exception.TokenExpiredException;
import com.regulareedge.authservice.repository.PasswordResetTokenRepository;
import com.regulareedge.authservice.repository.UserRepository;
import com.regulareedge.authservice.service.interfaces.AuditLogService;
import com.regulareedge.authservice.service.interfaces.PasswordResetService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final long RESET_TOKEN_VALIDITY_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public PasswordResetServiceImpl(UserRepository userRepository,
                                     PasswordResetTokenRepository passwordResetTokenRepository,
                                     PasswordEncoder passwordEncoder,
                                     AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    @Override
    public MessageResponse initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getUserId());
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(RESET_TOKEN_VALIDITY_MINUTES));
        resetToken.setUsed(false);

        passwordResetTokenRepository.save(resetToken);

        auditLogService.log(user.getUserId(), "PASSWORD_RESET_REQUESTED", "User", user.getUserId());

        return new MessageResponse("Password reset instructions have been generated for " + email);
    }

    @Override
    public MessageResponse resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenExpiredException("Password reset token is invalid"));

        if (resetToken.isUsed()) {
            throw new TokenExpiredException("Password reset token has already been used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Password reset token has expired");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        auditLogService.log(user.getUserId(), "PASSWORD_RESET_COMPLETED", "User", user.getUserId());

        return new MessageResponse("Password has been reset successfully");
    }
}
