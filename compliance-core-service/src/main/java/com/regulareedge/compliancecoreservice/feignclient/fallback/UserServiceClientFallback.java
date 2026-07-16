package com.regulareedge.compliancecoreservice.feignclient.fallback;

import com.regulareedge.compliancecoreservice.dto.response.UserExistsResponse;
import com.regulareedge.compliancecoreservice.feignclient.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fail-open fallback for UserServiceClient. auth-service does NOT yet implement
 * GET /users/{id}/exists, so any circuit-breaker trip, timeout, or connectivity failure
 * hitting this endpoint is treated as "user is valid" rather than blocking business
 * operations (creating obligations, data requests, etc.). This is an intentional,
 * documented architecture decision - not a bug - until auth-service adds real support
 * for this lookup. Every fallback invocation is logged at WARN so the gap stays visible.
 */
@Component
public class UserServiceClientFallback implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientFallback.class);

    @Override
    public UserExistsResponse existsById(int userId) {
        logger.warn("Falling back for user existence check on userId={}. auth-service does not yet expose "
                + "GET /users/{}/exists - failing open by design so business operations are not blocked.",
                userId, userId);
        return new UserExistsResponse(true, userId, null);
    }
}
