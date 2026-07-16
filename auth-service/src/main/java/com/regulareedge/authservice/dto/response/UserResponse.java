package com.regulareedge.authservice.dto.response;

import com.regulareedge.authservice.common.enums.UserRole;
import com.regulareedge.authservice.common.enums.UserStatus;

public class UserResponse {

    private int userId;
    private String name;
    private String email;
    private String phone;
    private int businessUnitId;
    private UserRole role;
    private UserStatus status;

    public UserResponse() {
    }

    public UserResponse(int userId, String name, String email, String phone, int businessUnitId,
                         UserRole role, UserStatus status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.businessUnitId = businessUnitId;
        this.role = role;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
