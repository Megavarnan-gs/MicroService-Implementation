package com.regulareedge.returnmanagementservice.dto.response;

public class CcoDashboardResponse {

    private int pendingApprovals;
    private int openPenalties;
    private double totalPenaltyAmount;

    public CcoDashboardResponse() {
    }

    public CcoDashboardResponse(int pendingApprovals, int openPenalties, double totalPenaltyAmount) {
        this.pendingApprovals = pendingApprovals;
        this.openPenalties = openPenalties;
        this.totalPenaltyAmount = totalPenaltyAmount;
    }

    public int getPendingApprovals() {
        return pendingApprovals;
    }

    public void setPendingApprovals(int pendingApprovals) {
        this.pendingApprovals = pendingApprovals;
    }

    public int getOpenPenalties() {
        return openPenalties;
    }

    public void setOpenPenalties(int openPenalties) {
        this.openPenalties = openPenalties;
    }

    public double getTotalPenaltyAmount() {
        return totalPenaltyAmount;
    }

    public void setTotalPenaltyAmount(double totalPenaltyAmount) {
        this.totalPenaltyAmount = totalPenaltyAmount;
    }
}
