package com.regulareedge.returnmanagementservice.dto.request;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class UpdatePenaltyStatusRequest {

    @NotNull(message = "penaltyId is required")
    @Positive(message = "penaltyId must be positive")
    private Integer penaltyId;

    @NotNull(message = "status is required")
    private PenaltyStatus status;

    private LocalDate paidDate;

    public Integer getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(Integer penaltyId) {
        this.penaltyId = penaltyId;
    }

    public PenaltyStatus getStatus() {
        return status;
    }

    public void setStatus(PenaltyStatus status) {
        this.status = status;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }
}
