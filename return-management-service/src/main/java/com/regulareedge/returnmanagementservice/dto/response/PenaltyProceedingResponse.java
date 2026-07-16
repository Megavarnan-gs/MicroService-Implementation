package com.regulareedge.returnmanagementservice.dto.response;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;

import java.time.LocalDate;

public class PenaltyProceedingResponse {

    private int penaltyId;
    private int correspondenceId;
    private int obligationId;
    private String penaltyType;
    private double penaltyAmount;
    private String disputeStatus;
    private LocalDate paidDate;
    private PenaltyStatus status;

    public PenaltyProceedingResponse() {
    }

    public PenaltyProceedingResponse(int penaltyId, int correspondenceId, int obligationId, String penaltyType,
                                      double penaltyAmount, String disputeStatus, LocalDate paidDate,
                                      PenaltyStatus status) {
        this.penaltyId = penaltyId;
        this.correspondenceId = correspondenceId;
        this.obligationId = obligationId;
        this.penaltyType = penaltyType;
        this.penaltyAmount = penaltyAmount;
        this.disputeStatus = disputeStatus;
        this.paidDate = paidDate;
        this.status = status;
    }

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

    public PenaltyStatus getStatus() {
        return status;
    }

    public void setStatus(PenaltyStatus status) {
        this.status = status;
    }
}
