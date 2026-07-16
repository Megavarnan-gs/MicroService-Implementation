package com.regulareedge.notificationservice.service.implementation;

import com.regulareedge.notificationservice.dto.response.UserExistsResponse;
import com.regulareedge.notificationservice.feignclient.UserServiceClient;
import com.regulareedge.notificationservice.service.interfaces.UserValidationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserValidationServiceImpl implements UserValidationService {

    private static final Logger logger = LoggerFactory.getLogger(UserValidationServiceImpl.class);

    private final UserServiceClient userServiceClient;

    public UserValidationServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    /**
     * NOTE: auth-service does not yet expose GET /users/{id}/exists. The Feign fallback
     * (UserServiceClientFallback) and the fallbackMethod below both fail open (treat the
     * user as valid) rather than blocking notification creation, per architecture doc.
     * This is intentional, not a stub left unfinished.
     */
    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "userExistsFallback")
    @Retry(name = "userService")
    public boolean userExists(int userId) {
        UserExistsResponse response = userServiceClient.existsById(userId);
        return response != null && response.isExists();
    }

    @SuppressWarnings("unused")
    private boolean userExistsFallback(int userId, Throwable throwable) {
        logger.warn("userExists check failed for userId={} ({}). Failing open by design since "
                + "auth-service does not yet expose GET /users/{}/exists.",
                userId, throwable.getMessage(), userId);
        return true;
    }
}
