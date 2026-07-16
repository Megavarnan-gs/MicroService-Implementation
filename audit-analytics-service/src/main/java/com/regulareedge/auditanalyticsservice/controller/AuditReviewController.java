package com.regulareedge.auditanalyticsservice.controller;

import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.dto.request.AuditReviewRequest;
import com.regulareedge.auditanalyticsservice.dto.response.AuditReviewResponse;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit-reviews")
@Validated
@Tag(name = "Audit Review Management")
public class AuditReviewController {

    private final AuditReviewService auditReviewService;

    public AuditReviewController(AuditReviewService auditReviewService) {
        this.auditReviewService = auditReviewService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('AUDITOR')")
    public ResponseEntity<AuditReviewResponse> add(@Valid @RequestBody AuditReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(auditReviewService.add(request));
    }

    @GetMapping("/myReviews/{auditorId}")
    @PreAuthorize("hasRole('AUDITOR')")
    public ResponseEntity<List<AuditReviewResponse>> myReviews(@PathVariable @Positive int auditorId) {
        return ResponseEntity.ok(auditReviewService.getMyReviews(auditorId));
    }

    @GetMapping("/getByRating")
    @PreAuthorize("hasAnyRole('AUDITOR', 'CCO')")
    public ResponseEntity<List<AuditReviewResponse>> getByRating(@RequestParam @NotNull AuditRating rating) {
        return ResponseEntity.ok(auditReviewService.getByRating(rating));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'CCO')")
    public ResponseEntity<List<AuditReviewResponse>> getAll() {
        return ResponseEntity.ok(auditReviewService.getAll());
    }
}
