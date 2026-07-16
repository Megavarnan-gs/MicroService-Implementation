package com.regulareedge.auditanalyticsservice.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.common.enums.ReportScope;
import com.regulareedge.auditanalyticsservice.dto.request.GenerateReportRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ComplianceReportResponse;
import com.regulareedge.auditanalyticsservice.dto.response.ObligationRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.entity.ComplianceReport;
import com.regulareedge.auditanalyticsservice.exception.DownstreamServiceException;
import com.regulareedge.auditanalyticsservice.mapper.ComplianceReportMapper;
import com.regulareedge.auditanalyticsservice.repository.ComplianceReportRepository;
import com.regulareedge.auditanalyticsservice.service.interfaces.ComplianceCoreQueryService;
import com.regulareedge.auditanalyticsservice.service.interfaces.ComplianceReportService;
import com.regulareedge.auditanalyticsservice.service.interfaces.ReturnManagementQueryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds compliance report "metrics" by best-effort aggregating data from
 * compliance-core-service (obligations) and return-management-service (returns,
 * penalties) via the resilience4j-wrapped query services, then serializes the
 * aggregated numbers into a JSON string stored on ComplianceReport#metrics. Any
 * degraded upstream call is reflected in the metrics payload itself (a "degraded"
 * boolean field) rather than blocking report generation, per the fail-open aggregation
 * architecture used throughout this service.
 */
@Service
public class ComplianceReportServiceImpl implements ComplianceReportService {

    private final ComplianceReportRepository complianceReportRepository;
    private final ReturnManagementQueryService returnManagementQueryService;
    private final ComplianceCoreQueryService complianceCoreQueryService;
    private final ObjectMapper objectMapper;

    public ComplianceReportServiceImpl(ComplianceReportRepository complianceReportRepository,
                                        ReturnManagementQueryService returnManagementQueryService,
                                        ComplianceCoreQueryService complianceCoreQueryService,
                                        ObjectMapper objectMapper) {
        this.complianceReportRepository = complianceReportRepository;
        this.returnManagementQueryService = returnManagementQueryService;
        this.complianceCoreQueryService = complianceCoreQueryService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ComplianceReportResponse generate(GenerateReportRequest request) {
        ReportScope scope = request.getScope();
        Map<String, Object> metrics = new LinkedHashMap<>();
        boolean degraded = false;

        if (scope == ReportScope.OBLIGATION || scope == ReportScope.OVERALL) {
            FeignResult<List<ObligationRefResponse>> obligations = complianceCoreQueryService.getAllObligations();
            metrics.put("totalObligations", obligations.getData().size());
            degraded = degraded || obligations.isDegraded();
        }

        if (scope == ReportScope.RETURN || scope == ReportScope.OVERALL) {
            FeignResult<List<RegulatoryReturnRefResponse>> pendingReturns =
                    returnManagementQueryService.getReturnsByStatus("PENDING_CCO_APPROVAL");
            metrics.put("pendingApprovals", pendingReturns.getData().size());
            degraded = degraded || pendingReturns.isDegraded();
        }

        if (scope == ReportScope.PENALTY || scope == ReportScope.OVERALL) {
            FeignResult<List<PenaltyProceedingRefResponse>> allPenalties =
                    returnManagementQueryService.getAllPenalties();
            List<PenaltyProceedingRefResponse> penalties = allPenalties.getData();
            double totalPenaltyAmount = penalties.stream()
                    .mapToDouble(PenaltyProceedingRefResponse::getPenaltyAmount)
                    .sum();
            metrics.put("totalPenalties", penalties.size());
            metrics.put("totalPenaltyAmount", totalPenaltyAmount);
            degraded = degraded || allPenalties.isDegraded();
        }

        metrics.put("degraded", degraded);

        String metricsJson;
        try {
            metricsJson = objectMapper.writeValueAsString(metrics);
        } catch (Exception ex) {
            throw new DownstreamServiceException("Failed to serialize compliance report metrics", ex);
        }

        ComplianceReport report = new ComplianceReport();
        report.setScope(scope);
        report.setMetrics(metricsJson);
        report.setGeneratedDate(LocalDate.now());

        ComplianceReport saved = complianceReportRepository.save(report);
        return ComplianceReportMapper.toResponse(saved);
    }

    @Override
    public List<ComplianceReportResponse> getAll() {
        return complianceReportRepository.findAll().stream()
                .map(ComplianceReportMapper::toResponse)
                .toList();
    }

    @Override
    public List<ComplianceReportResponse> getByScope(ReportScope scope) {
        return complianceReportRepository.findByScope(scope).stream()
                .map(ComplianceReportMapper::toResponse)
                .toList();
    }
}
