package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.dto.response.CcoDashboardResponse;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;

import java.util.List;

/**
 * Replicates the monolith's CCOController aggregation, but sourced via Feign from
 * return-management-service instead of in-process repository calls.
 */
public interface DashboardAggregationService {

    CcoDashboardResponse getSummary();

    List<RegulatoryReturnRefResponse> getPendingReturns();

    List<PenaltyProceedingRefResponse> getAllPenalties();
}
