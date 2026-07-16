package com.regulareedge.auditanalyticsservice.repository;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.entity.AuditReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditReviewRepository extends JpaRepository<AuditReview, Integer> {

    List<AuditReview> findByAuditorId(int auditorId);

    List<AuditReview> findByRating(AuditRating rating);

    Page<AuditReview> findByRating(AuditRating rating, Pageable pageable);
}
