package com.regulareedge.compliancecoreservice.feignclient;

import com.regulareedge.compliancecoreservice.config.FeignConfig;
import com.regulareedge.compliancecoreservice.dto.response.UserExistsResponse;
import com.regulareedge.compliancecoreservice.feignclient.fallback.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client for auth-service's user lookup contract. NOTE: GET /users/{userId}/exists does not
 * exist in auth-service yet (auth-service currently only exposes GET /users and
 * GET /users/role/{role}). This client assumes the contract described in the architecture
 * doc so that compliance-core-service can be built ahead of that endpoint landing.
 * See UserServiceClientFallback for the fail-open behaviour this implies.
 */
@FeignClient(name = "auth-service", configuration = FeignConfig.class, fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/users/{userId}/exists")
    UserExistsResponse existsById(@PathVariable("userId") int userId);
}
