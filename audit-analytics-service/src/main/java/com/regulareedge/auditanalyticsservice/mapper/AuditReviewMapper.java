package com.regulareedge.auditanalyticsservice.mapper;

import com.regulareedge.auditanalyticsservice.dto.response.AuditReviewResponse;
import com.regulareedge.auditanalyticsservice.entity.AuditReview;

public final class AuditReviewMapper {

    private AuditReviewMapper() {
    }

    public static AuditReviewResponse toResponse(AuditReview entity) {
        if (entity == null) {
            return null;
        }
        return new AuditReviewResponse(
                entity.getReviewId(),
                entity.getObligationId(),
                entity.getAuditorId(),
                entity.getReviewPeriod(),
                entity.getScope(),
                entity.getFindingsSummary(),
                entity.getRating(),
                entity.getReviewDate(),
                entity.getStatus());
    }
}
