package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.dto.request.AuditReviewRequest;
import com.regulareedge.auditanalyticsservice.dto.response.AuditReviewResponse;

import java.util.List;

public interface AuditReviewService {

    AuditReviewResponse add(AuditReviewRequest request);

    List<AuditReviewResponse> getMyReviews(int auditorId);

    List<AuditReviewResponse> getByRating(AuditRating rating);

    List<AuditReviewResponse> getAll();
}
