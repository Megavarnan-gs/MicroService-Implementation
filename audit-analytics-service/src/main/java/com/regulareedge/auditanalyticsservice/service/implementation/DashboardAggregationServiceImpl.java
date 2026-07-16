package com.regulareedge.auditanalyticsservice.service.implementation;

import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.dto.response.CcoDashboardResponse;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.service.interfaces.DashboardAggregationService;
import com.regulareedge.auditanalyticsservice.service.interfaces.ReturnManagementQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Mirrors the monolith's CCOController#getDashboard aggregation, but computed via
 * best-effort Feign calls to return-management-service (see ReturnManagementQueryService)
 * since this service does not own the RegulatoryReturn/PenaltyProceeding tables. If any
 * upstream call falls back, the resulting response is flagged degraded=true rather than
 * silently presenting fallback data as authoritative.
 */
@Service
public class DashboardAggregationServiceImpl implements DashboardAggregationService {

    private static final String PENDING_CCO_APPROVAL = "PENDING_CCO_APPROVAL";
    private static final String OPEN = "OPEN";

    private final ReturnManagementQueryService returnManagementQueryService;

    public DashboardAggregationServiceImpl(ReturnManagementQueryService returnManagementQueryService) {
        this.returnManagementQueryService = returnManagementQueryService;
    }

    @Override
    public CcoDashboardResponse getSummary() {
        FeignResult<List<RegulatoryReturnRefResponse>> pendingReturns =
                returnManagementQueryService.getReturnsByStatus(PENDING_CCO_APPROVAL);
        FeignResult<List<PenaltyProceedingRefResponse>> openPenalties =
                returnManagementQueryService.getPenaltiesByStatus(OPEN);

        int pendingApprovals = pendingReturns.getData().size();
        List<PenaltyProceedingRefResponse> penalties = openPenalties.getData();
        double totalPenaltyAmount = penalties.stream()
                .mapToDouble(PenaltyProceedingRefResponse::getPenaltyAmount)
                .sum();

        boolean degraded = pendingReturns.isDegraded() || openPenalties.isDegraded();

        return new CcoDashboardResponse(pendingApprovals, penalties.size(), totalPenaltyAmount, degraded);
    }

    @Override
    public List<RegulatoryReturnRefResponse> getPendingReturns() {
        return returnManagementQueryService.getReturnsByStatus(PENDING_CCO_APPROVAL).getData();
    }

    @Override
    public List<PenaltyProceedingRefResponse> getAllPenalties() {
        return returnManagementQueryService.getAllPenalties().getData();
    }
}
