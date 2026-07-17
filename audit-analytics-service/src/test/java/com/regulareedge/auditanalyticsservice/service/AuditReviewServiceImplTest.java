package com.regulareedge.auditanalyticsservice.service;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.common.enums.AuditReviewStatus;
import com.regulareedge.auditanalyticsservice.dto.request.AuditReviewRequest;
import com.regulareedge.auditanalyticsservice.dto.response.AuditReviewResponse;
import com.regulareedge.auditanalyticsservice.entity.AuditReview;
import com.regulareedge.auditanalyticsservice.repository.AuditReviewRepository;
import com.regulareedge.auditanalyticsservice.service.implementation.AuditReviewServiceImpl;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditReviewServiceImplTest {

    @Mock
    private AuditReviewRepository auditReviewRepository;

    @Mock
    private AuditLogService auditLogService;

    private AuditReviewServiceImpl auditReviewService;

    private AuditReview review;

    @BeforeEach
    void setUp() {
        auditReviewService = new AuditReviewServiceImpl(auditReviewRepository, auditLogService);

        review = new AuditReview();
        review.setReviewId(1);
        review.setObligationId(10);
        review.setAuditorId(5);
        review.setReviewPeriod("2026-Q1");
        review.setScope("Full");
        review.setFindingsSummary("All good");
        review.setRating(AuditRating.SATISFACTORY);
        review.setReviewDate(LocalDate.of(2026, 3, 1));
        review.setStatus(AuditReviewStatus.DRAFT);
    }

    @Test
    void add_shouldPersistReviewAsDraft() {
        AuditReviewRequest request = new AuditReviewRequest();
        request.setObligationId(10);
        request.setAuditorId(5);
        request.setReviewPeriod("2026-Q1");
        request.setScope("Full");
        request.setFindingsSummary("All good");
        request.setRating(AuditRating.SATISFACTORY);
        request.setReviewDate(LocalDate.of(2026, 3, 1));

        when(auditReviewRepository.save(any(AuditReview.class))).thenReturn(review);

        AuditReviewResponse response = auditReviewService.add(request);

        ArgumentCaptor<AuditReview> captor = ArgumentCaptor.forClass(AuditReview.class);
        verify(auditReviewRepository).save(captor.capture());

        assertEquals(AuditReviewStatus.DRAFT, captor.getValue().getStatus());
        assertEquals(AuditRating.SATISFACTORY, response.getRating());
        assertEquals(10, response.getObligationId());

        verify(auditLogService).log(5, "AUDIT_REVIEW_CREATED", "AuditReview", 1);
    }

    @Test
    void getMyReviews_shouldReturnReviewsForAuditor() {
        when(auditReviewRepository.findByAuditorId(5)).thenReturn(List.of(review));

        List<AuditReviewResponse> responses = auditReviewService.getMyReviews(5);

        assertEquals(1, responses.size());
        assertEquals(5, responses.get(0).getAuditorId());
    }

    @Test
    void getByRating_shouldReturnMatchingReviews() {
        when(auditReviewRepository.findByRating(AuditRating.SATISFACTORY)).thenReturn(List.of(review));

        List<AuditReviewResponse> responses = auditReviewService.getByRating(AuditRating.SATISFACTORY);

        assertEquals(1, responses.size());
        assertEquals(AuditRating.SATISFACTORY, responses.get(0).getRating());
    }

    @Test
    void getAll_shouldReturnAllReviews() {
        when(auditReviewRepository.findAll()).thenReturn(List.of(review));

        List<AuditReviewResponse> responses = auditReviewService.getAll();

        assertEquals(1, responses.size());
    }
}
