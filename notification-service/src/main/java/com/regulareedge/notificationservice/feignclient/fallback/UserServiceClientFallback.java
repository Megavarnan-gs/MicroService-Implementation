package com.regulareedge.notificationservice.feignclient.fallback;

import com.regulareedge.notificationservice.dto.response.UserExistsResponse;
import com.regulareedge.notificationservice.feignclient.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientFallback.class);

    @Override
    public UserExistsResponse existsById(int userId) {
        logger.warn("Falling back for user existence check on userId={}. auth-service does not yet expose "
                + "GET /users/{}/exists - failing open by design so notification creation is not blocked.",
                userId, userId);
        return new UserExistsResponse(true, userId, null);
    }
}
