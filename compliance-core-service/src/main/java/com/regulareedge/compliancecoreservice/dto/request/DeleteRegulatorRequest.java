package com.regulareedge.compliancecoreservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DeleteRegulatorRequest {

    @NotNull(message = "regulatorId is required")
    @Positive(message = "regulatorId must be positive")
    private Integer regulatorId;

    public Integer getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(Integer regulatorId) {
        this.regulatorId = regulatorId;
    }
}
