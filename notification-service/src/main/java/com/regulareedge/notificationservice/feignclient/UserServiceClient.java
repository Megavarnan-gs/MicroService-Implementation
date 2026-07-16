package com.regulareedge.notificationservice.feignclient;

import com.regulareedge.notificationservice.config.FeignConfig;
import com.regulareedge.notificationservice.dto.response.UserExistsResponse;
import com.regulareedge.notificationservice.feignclient.fallback.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client for auth-service's user lookup contract. NOTE: GET /users/{userId}/exists does not
 * exist in auth-service yet. This client assumes the contract described in the architecture
 * doc so that notification-service can be built ahead of that endpoint landing.
 * See UserServiceClientFallback for the fail-open behaviour this implies.
 */
@FeignClient(name = "auth-service", configuration = FeignConfig.class, fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/users/{userId}/exists")
    UserExistsResponse existsById(@PathVariable("userId") int userId);
}
