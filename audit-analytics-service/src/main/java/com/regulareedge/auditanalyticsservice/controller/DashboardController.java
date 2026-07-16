package com.regulareedge.auditanalyticsservice.controller;

import com.regulareedge.auditanalyticsservice.dto.response.CcoDashboardResponse;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.service.interfaces.DashboardAggregationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Mirrors the monolith's CCOController exactly, but sourced via Feign from
 * return-management-service instead of in-process calls.
 */
@RestController
@RequestMapping("/analytics")
@Validated
@Tag(name = "CCO Dashboard")
public class DashboardController {

    private final DashboardAggregationService dashboardAggregationService;

    public DashboardController(DashboardAggregationService dashboardAggregationService) {
        this.dashboardAggregationService = dashboardAggregationService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<CcoDashboardResponse> dashboard() {
        return ResponseEntity.ok(dashboardAggregationService.getSummary());
    }

    @GetMapping("/pending-returns")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<List<RegulatoryReturnRefResponse>> pendingReturns() {
        return ResponseEntity.ok(dashboardAggregationService.getPendingReturns());
    }

    @GetMapping("/all-penalties")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<List<PenaltyProceedingRefResponse>> allPenalties() {
        return ResponseEntity.ok(dashboardAggregationService.getAllPenalties());
    }
}
