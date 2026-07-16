package com.regulareedge.auditanalyticsservice.feignclient;

import com.regulareedge.auditanalyticsservice.config.FeignConfig;
import com.regulareedge.auditanalyticsservice.dto.response.ObligationRefResponse;
import com.regulareedge.auditanalyticsservice.feignclient.fallback.ComplianceCoreServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Client for compliance-core-service's real, already-implemented endpoint, used by this
 * best-effort aggregator/oversight layer to build obligation counts/metrics for
 * compliance reports. This is a pure aggregation dependency, not a hard requirement -
 * see ComplianceCoreServiceClientFallback for the fail-open behaviour applied when
 * compliance-core-service is unreachable or slow.
 */
@FeignClient(name = "compliance-core-service", configuration = FeignConfig.class,
        fallback = ComplianceCoreServiceClientFallback.class)
public interface ComplianceCoreServiceClient {

    @GetMapping("/obligations/getAll")
    List<ObligationRefResponse> getAllObligations();
}
