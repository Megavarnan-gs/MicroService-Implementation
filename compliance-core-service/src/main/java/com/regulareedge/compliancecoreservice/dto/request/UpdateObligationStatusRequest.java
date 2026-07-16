package com.regulareedge.compliancecoreservice.dto.request;

import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateObligationStatusRequest {

    @NotNull(message = "obligationId is required")
    @Positive(message = "obligationId must be positive")
    private Integer obligationId;

    @NotNull(message = "status is required")
    private ObligationStatus status;

    public Integer getObligationId() {
        return obligationId;
    }

    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }

    public ObligationStatus getStatus() {
        return status;
    }

    public void setStatus(ObligationStatus status) {
        this.status = status;
    }
}
