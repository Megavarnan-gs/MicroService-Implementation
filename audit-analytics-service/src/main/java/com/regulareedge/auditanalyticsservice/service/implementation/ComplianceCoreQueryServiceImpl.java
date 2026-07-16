package com.regulareedge.auditanalyticsservice.service.implementation;

import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.dto.response.ObligationRefResponse;
import com.regulareedge.auditanalyticsservice.feignclient.ComplianceCoreServiceClient;
import com.regulareedge.auditanalyticsservice.service.interfaces.ComplianceCoreQueryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplianceCoreQueryServiceImpl implements ComplianceCoreQueryService {

    private static final Logger logger = LoggerFactory.getLogger(ComplianceCoreQueryServiceImpl.class);

    private final ComplianceCoreServiceClient complianceCoreServiceClient;

    public ComplianceCoreQueryServiceImpl(ComplianceCoreServiceClient complianceCoreServiceClient) {
        this.complianceCoreServiceClient = complianceCoreServiceClient;
    }

    @Override
    @CircuitBreaker(name = "complianceCoreService", fallbackMethod = "getAllObligationsFallback")
    @Retry(name = "complianceCoreService")
    public FeignResult<List<ObligationRefResponse>> getAllObligations() {
        return FeignResult.ok(complianceCoreServiceClient.getAllObligations());
    }

    @SuppressWarnings("unused")
    private FeignResult<List<ObligationRefResponse>> getAllObligationsFallback(Throwable throwable) {
        logger.warn("getAllObligations() failed against compliance-core-service ({}). "
                + "Failing open by design - result will be flagged degraded.", throwable.getMessage());
        return FeignResult.degraded(List.of());
    }
}
