package com.regulareedge.auditanalyticsservice.service.implementation;

import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.feignclient.ReturnManagementServiceClient;
import com.regulareedge.auditanalyticsservice.service.interfaces.ReturnManagementQueryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service is a pure cross-service aggregator/oversight layer, so every call to
 * return-management-service is best-effort: both the Feign fallback and the
 * resilience4j fallbackMethod below fail open (return an empty list, flagged as
 * degraded) rather than blocking dashboard/report generation.
 */
@Service
public class ReturnManagementQueryServiceImpl implements ReturnManagementQueryService {

    private static final Logger logger = LoggerFactory.getLogger(ReturnManagementQueryServiceImpl.class);

    private final ReturnManagementServiceClient returnManagementServiceClient;

    public ReturnManagementQueryServiceImpl(ReturnManagementServiceClient returnManagementServiceClient) {
        this.returnManagementServiceClient = returnManagementServiceClient;
    }

    @Override
    @CircuitBreaker(name = "returnManagementService", fallbackMethod = "getReturnsByStatusFallback")
    @Retry(name = "returnManagementService")
    public FeignResult<List<RegulatoryReturnRefResponse>> getReturnsByStatus(String status) {
        return FeignResult.ok(returnManagementServiceClient.getReturnsByStatus(status));
    }

    @SuppressWarnings("unused")
    private FeignResult<List<RegulatoryReturnRefResponse>> getReturnsByStatusFallback(String status,
                                                                                       Throwable throwable) {
        logger.warn("getReturnsByStatus(status={}) failed against return-management-service ({}). "
                + "Failing open by design - result will be flagged degraded.", status, throwable.getMessage());
        return FeignResult.degraded(List.of());
    }

    @Override
    @CircuitBreaker(name = "returnManagementService", fallbackMethod = "getPenaltiesByStatusFallback")
    @Retry(name = "returnManagementService")
    public FeignResult<List<PenaltyProceedingRefResponse>> getPenaltiesByStatus(String status) {
        return FeignResult.ok(returnManagementServiceClient.getPenaltiesByStatus(status));
    }

    @SuppressWarnings("unused")
    private FeignResult<List<PenaltyProceedingRefResponse>> getPenaltiesByStatusFallback(String status,
                                                                                          Throwable throwable) {
        logger.warn("getPenaltiesByStatus(status={}) failed against return-management-service ({}). "
                + "Failing open by design - result will be flagged degraded.", status, throwable.getMessage());
        return FeignResult.degraded(List.of());
    }

    @Override
    @CircuitBreaker(name = "returnManagementService", fallbackMethod = "getAllPenaltiesFallback")
    @Retry(name = "returnManagementService")
    public FeignResult<List<PenaltyProceedingRefResponse>> getAllPenalties() {
        return FeignResult.ok(returnManagementServiceClient.getAllPenalties());
    }

    @SuppressWarnings("unused")
    private FeignResult<List<PenaltyProceedingRefResponse>> getAllPenaltiesFallback(Throwable throwable) {
        logger.warn("getAllPenalties() failed against return-management-service ({}). "
                + "Failing open by design - result will be flagged degraded.", throwable.getMessage());
        return FeignResult.degraded(List.of());
    }
}
