package com.regulareedge.auditanalyticsservice.feignclient;

import com.regulareedge.auditanalyticsservice.config.FeignConfig;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.feignclient.fallback.ReturnManagementServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Client for return-management-service's real, already-implemented endpoints, used by
 * this best-effort aggregator/oversight layer to build the CCO dashboard and compliance
 * reports. This is a pure aggregation dependency, not a hard requirement - see
 * ReturnManagementServiceClientFallback for the fail-open behaviour applied when
 * return-management-service is unreachable or slow.
 */
@FeignClient(name = "return-management-service", configuration = FeignConfig.class,
        fallback = ReturnManagementServiceClientFallback.class)
public interface ReturnManagementServiceClient {

    @GetMapping("/returns/getByStatus")
    List<RegulatoryReturnRefResponse> getReturnsByStatus(@RequestParam("status") String status);

    @GetMapping("/penalties/getByStatus")
    List<PenaltyProceedingRefResponse> getPenaltiesByStatus(@RequestParam("status") String status);

    @GetMapping("/penalties/getAll")
    List<PenaltyProceedingRefResponse> getAllPenalties();
}
