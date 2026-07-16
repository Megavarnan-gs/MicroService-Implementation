package com.regulareedge.auditanalyticsservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

/**
 * Slim projection of return-management-service's PenaltyProceedingResponse. Only the
 * fields needed by this service's aggregation/dashboard logic are declared; unknown
 * properties returned by the real endpoint are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PenaltyProceedingRefResponse {

    private int penaltyId;
    private int correspondenceId;
    private int obligationId;
    private String penaltyType;
    private double penaltyAmount;
    private String disputeStatus;
    private LocalDate paidDate;
    private String status;

    public int getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(int penaltyId) {
        this.penaltyId = penaltyId;
    }

    public int getCorrespondenceId() {
        return correspondenceId;
    }

    public void setCorrespondenceId(int correspondenceId) {
        this.correspondenceId = correspondenceId;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public String getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
