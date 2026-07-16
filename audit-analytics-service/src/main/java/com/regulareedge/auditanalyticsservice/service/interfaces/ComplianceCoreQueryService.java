package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.dto.response.ObligationRefResponse;

import java.util.List;

/**
 * Resilience4j-wrapped facade over ComplianceCoreServiceClient, used by
 * ComplianceReportService to build obligation-related metrics.
 */
public interface ComplianceCoreQueryService {

    FeignResult<List<ObligationRefResponse>> getAllObligations();
}
