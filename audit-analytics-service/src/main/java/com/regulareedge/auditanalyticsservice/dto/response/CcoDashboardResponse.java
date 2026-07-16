package com.regulareedge.auditanalyticsservice.dto.response;

public class CcoDashboardResponse {

    private int pendingApprovals;
    private int openPenalties;
    private double totalPenaltyAmount;

    /** True if any upstream Feign call failed and fallback (degraded) data was used. */
    private boolean degraded;

    public CcoDashboardResponse() {
    }

    public CcoDashboardResponse(int pendingApprovals, int openPenalties, double totalPenaltyAmount,
                                 boolean degraded) {
        this.pendingApprovals = pendingApprovals;
        this.openPenalties = openPenalties;
        this.totalPenaltyAmount = totalPenaltyAmount;
        this.degraded = degraded;
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

    public boolean isDegraded() {
        return degraded;
    }

    public void setDegraded(boolean degraded) {
        this.degraded = degraded;
    }
}
