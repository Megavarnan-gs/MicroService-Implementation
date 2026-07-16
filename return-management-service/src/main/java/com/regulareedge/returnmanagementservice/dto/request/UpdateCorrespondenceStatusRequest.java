package com.regulareedge.returnmanagementservice.dto.request;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateCorrespondenceStatusRequest {

    @NotNull(message = "correspondenceId is required")
    @Positive(message = "correspondenceId must be positive")
    private Integer correspondenceId;

    @NotNull(message = "status is required")
    private CorrespondenceStatus status;

    public Integer getCorrespondenceId() {
        return correspondenceId;
    }

    public void setCorrespondenceId(Integer correspondenceId) {
        this.correspondenceId = correspondenceId;
    }

    public CorrespondenceStatus getStatus() {
        return status;
    }

    public void setStatus(CorrespondenceStatus status) {
        this.status = status;
    }
}
