package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;

import java.util.List;

/**
 * Resilience4j-wrapped facade over ReturnManagementServiceClient. Every method is
 * annotated with @CircuitBreaker/@Retry and reports whether fallback data was used via
 * FeignResult#isDegraded, so callers (DashboardAggregationService, ComplianceReportService)
 * can propagate a "degraded" flag rather than silently trusting fallback data.
 */
public interface ReturnManagementQueryService {

    FeignResult<List<RegulatoryReturnRefResponse>> getReturnsByStatus(String status);

    FeignResult<List<PenaltyProceedingRefResponse>> getPenaltiesByStatus(String status);

    FeignResult<List<PenaltyProceedingRefResponse>> getAllPenalties();
}
