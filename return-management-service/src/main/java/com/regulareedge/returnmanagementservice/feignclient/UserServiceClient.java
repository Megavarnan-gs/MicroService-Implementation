package com.regulareedge.returnmanagementservice.feignclient;

import com.regulareedge.returnmanagementservice.config.FeignConfig;
import com.regulareedge.returnmanagementservice.dto.response.UserExistsResponse;
import com.regulareedge.returnmanagementservice.feignclient.fallback.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client for auth-service's user lookup contract. NOTE: GET /users/{userId}/exists does not
 * exist in auth-service yet. This client assumes the contract described in the architecture
 * doc so that return-management-service can be built ahead of that endpoint landing.
 * See UserServiceClientFallback for the fail-open behaviour this implies.
 */
@FeignClient(name = "auth-service", configuration = FeignConfig.class, fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/users/{userId}/exists")
    UserExistsResponse existsById(@PathVariable("userId") int userId);
}
