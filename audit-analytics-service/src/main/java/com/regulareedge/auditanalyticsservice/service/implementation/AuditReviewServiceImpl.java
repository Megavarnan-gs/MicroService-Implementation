package com.regulareedge.auditanalyticsservice.service.implementation;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.common.enums.AuditReviewStatus;
import com.regulareedge.auditanalyticsservice.dto.request.AuditReviewRequest;
import com.regulareedge.auditanalyticsservice.dto.response.AuditReviewResponse;
import com.regulareedge.auditanalyticsservice.entity.AuditReview;
import com.regulareedge.auditanalyticsservice.mapper.AuditReviewMapper;
import com.regulareedge.auditanalyticsservice.repository.AuditReviewRepository;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditReviewServiceImpl implements AuditReviewService {

    private final AuditReviewRepository auditReviewRepository;

    public AuditReviewServiceImpl(AuditReviewRepository auditReviewRepository) {
        this.auditReviewRepository = auditReviewRepository;
    }

    @Override
    public AuditReviewResponse add(AuditReviewRequest request) {
        AuditReview review = new AuditReview();
        review.setObligationId(request.getObligationId());
        review.setAuditorId(request.getAuditorId());
        review.setReviewPeriod(request.getReviewPeriod());
        review.setScope(request.getScope());
        review.setFindingsSummary(request.getFindingsSummary());
        review.setRating(request.getRating());
        review.setReviewDate(request.getReviewDate());
        review.setStatus(AuditReviewStatus.DRAFT);

        AuditReview saved = auditReviewRepository.save(review);
        return AuditReviewMapper.toResponse(saved);
    }

    @Override
    public List<AuditReviewResponse> getMyReviews(int auditorId) {
        return auditReviewRepository.findByAuditorId(auditorId).stream()
                .map(AuditReviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<AuditReviewResponse> getByRating(AuditRating rating) {
        return auditReviewRepository.findByRating(rating).stream()
                .map(AuditReviewMapper::toResponse)
                .toList();
    }

    @Override
    public List<AuditReviewResponse> getAll() {
        return auditReviewRepository.findAll().stream()
                .map(AuditReviewMapper::toResponse)
                .toList();
    }
}
