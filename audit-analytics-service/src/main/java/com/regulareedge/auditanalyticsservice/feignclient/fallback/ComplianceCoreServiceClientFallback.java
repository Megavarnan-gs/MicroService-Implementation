package com.regulareedge.auditanalyticsservice.feignclient.fallback;

import com.regulareedge.auditanalyticsservice.dto.response.ObligationRefResponse;
import com.regulareedge.auditanalyticsservice.feignclient.ComplianceCoreServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fail-open fallback for ComplianceCoreServiceClient. Obligation aggregation is
 * best-effort: if compliance-core-service is unreachable, slow, or its circuit breaker
 * is open, this fallback returns an empty list rather than blocking report generation.
 * Callers must treat an empty list as "could not aggregate" and mark the resulting
 * response as degraded rather than failing the request outright. Every fallback
 * invocation is logged at WARN so the gap stays visible.
 */
@Component
public class ComplianceCoreServiceClientFallback implements ComplianceCoreServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ComplianceCoreServiceClientFallback.class);

    @Override
    public List<ObligationRefResponse> getAllObligations() {
        logger.warn("Falling back for compliance-core-service getAllObligations(). Failing open by design - "
                + "obligation aggregation will be degraded for this request.");
        return List.of();
    }
}
