package com.regulareedge.returnmanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateReturnStatusRequest {

    @NotNull(message = "returnId is required")
    @Positive(message = "returnId must be positive")
    private Integer returnId;

    @NotBlank(message = "status is required")
    private String status;

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
