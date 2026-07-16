package com.regulareedge.auditanalyticsservice.dto.response;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.common.enums.AuditReviewStatus;

import java.time.LocalDate;

public class AuditReviewResponse {

    private int reviewId;
    private int obligationId;
    private int auditorId;
    private String reviewPeriod;
    private String scope;
    private String findingsSummary;
    private AuditRating rating;
    private LocalDate reviewDate;
    private AuditReviewStatus status;

    public AuditReviewResponse() {
    }

    public AuditReviewResponse(int reviewId, int obligationId, int auditorId, String reviewPeriod, String scope,
                                String findingsSummary, AuditRating rating, LocalDate reviewDate,
                                AuditReviewStatus status) {
        this.reviewId = reviewId;
        this.obligationId = obligationId;
        this.auditorId = auditorId;
        this.reviewPeriod = reviewPeriod;
        this.scope = scope;
        this.findingsSummary = findingsSummary;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.status = status;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public int getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(int auditorId) {
        this.auditorId = auditorId;
    }

    public String getReviewPeriod() {
        return reviewPeriod;
    }

    public void setReviewPeriod(String reviewPeriod) {
        this.reviewPeriod = reviewPeriod;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getFindingsSummary() {
        return findingsSummary;
    }

    public void setFindingsSummary(String findingsSummary) {
        this.findingsSummary = findingsSummary;
    }

    public AuditRating getRating() {
        return rating;
    }

    public void setRating(AuditRating rating) {
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public AuditReviewStatus getStatus() {
        return status;
    }

    public void setStatus(AuditReviewStatus status) {
        this.status = status;
    }
}
