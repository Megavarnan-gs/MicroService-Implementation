package com.regulareedge.auditanalyticsservice.entity;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.common.enums.AuditReviewStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "audit_review")
public class AuditReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    /** External id owned by compliance-core-service - plain int, no JPA relation. */
    private int obligationId;

    /** External id owned by auth-service - plain int, no JPA relation. */
    private int auditorId;

    private String reviewPeriod;
    private String scope;
    private String findingsSummary;

    @Enumerated(EnumType.STRING)
    private AuditRating rating;

    private LocalDate reviewDate;

    @Enumerated(EnumType.STRING)
    private AuditReviewStatus status;

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
