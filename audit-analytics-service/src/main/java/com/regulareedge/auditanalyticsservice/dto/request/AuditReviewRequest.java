package com.regulareedge.auditanalyticsservice.dto.request;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class AuditReviewRequest {

    @Positive
    private int obligationId;

    @Positive
    private int auditorId;

    @NotBlank
    private String reviewPeriod;

    @NotBlank
    private String scope;

    private String findingsSummary;

    @NotNull
    private AuditRating rating;

    @NotNull
    private LocalDate reviewDate;

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
}
