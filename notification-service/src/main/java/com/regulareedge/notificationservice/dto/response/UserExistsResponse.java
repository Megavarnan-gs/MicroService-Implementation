package com.regulareedge.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Contract assumed for a not-yet-implemented auth-service endpoint: GET /users/{id}/exists.
 * See feignclient.UserServiceClient and its fallback for the fail-open handling required
 * because this endpoint does not exist in auth-service yet.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserExistsResponse {

    private boolean exists;
    private int userId;
    private String role;

    public UserExistsResponse() {
    }

    public UserExistsResponse(boolean exists, int userId, String role) {
        this.exists = exists;
        this.userId = userId;
        this.role = role;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
