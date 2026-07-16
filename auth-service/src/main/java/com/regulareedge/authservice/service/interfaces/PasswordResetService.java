package com.regulareedge.authservice.service.interfaces;

import com.regulareedge.authservice.dto.response.MessageResponse;

public interface PasswordResetService {

    MessageResponse initiatePasswordReset(String email);

    MessageResponse resetPassword(String token, String newPassword);
}
