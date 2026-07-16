package com.regulareedge.returnmanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class PenaltyProceedingRequest {

    @NotNull(message = "correspondenceId is required")
    @Positive(message = "correspondenceId must be positive")
    private Integer correspondenceId;

    @NotNull(message = "obligationId is required")
    @Positive(message = "obligationId must be positive")
    private Integer obligationId;

    @NotBlank(message = "penaltyType is required")
    private String penaltyType;

    @NotNull(message = "penaltyAmount is required")
    @PositiveOrZero(message = "penaltyAmount must not be negative")
    private Double penaltyAmount;

    private String disputeStatus;

    public Integer getCorrespondenceId() {
        return correspondenceId;
    }

    public void setCorrespondenceId(Integer correspondenceId) {
        this.correspondenceId = correspondenceId;
    }

    public Integer getObligationId() {
        return obligationId;
    }

    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }

    public String getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }
}
