package com.regulareedge.auditanalyticsservice.feignclient.fallback;

import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.feignclient.ReturnManagementServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fail-open fallback for ReturnManagementServiceClient. This service is a pure
 * cross-service aggregator/oversight layer: if return-management-service is unreachable,
 * slow, or its circuit breaker is open, this fallback returns an empty list rather than
 * blocking dashboard/report generation. Callers must treat an empty list as
 * "could not aggregate" and mark the resulting response as degraded rather than failing
 * the request outright. Every fallback invocation is logged at WARN so the gap stays
 * visible.
 */
@Component
public class ReturnManagementServiceClientFallback implements ReturnManagementServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ReturnManagementServiceClientFallback.class);

    @Override
    public List<RegulatoryReturnRefResponse> getReturnsByStatus(String status) {
        logger.warn("Falling back for return-management-service getReturnsByStatus(status={}). "
                + "Failing open by design - returns aggregation will be degraded for this request.", status);
        return List.of();
    }

    @Override
    public List<PenaltyProceedingRefResponse> getPenaltiesByStatus(String status) {
        logger.warn("Falling back for return-management-service getPenaltiesByStatus(status={}). "
                + "Failing open by design - penalty aggregation will be degraded for this request.", status);
        return List.of();
    }

    @Override
    public List<PenaltyProceedingRefResponse> getAllPenalties() {
        logger.warn("Falling back for return-management-service getAllPenalties(). "
                + "Failing open by design - penalty aggregation will be degraded for this request.");
        return List.of();
    }
}
