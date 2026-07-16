package com.regulareedge.authservice.service.interfaces;

import com.regulareedge.authservice.entity.RefreshToken;

public interface TokenService {

    RefreshToken createRefreshToken(int userId, String token);

    RefreshToken verifyRefreshToken(String token);

    void revokeRefreshTokensForUser(int userId);
}
