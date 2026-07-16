package com.regulareedge.auditanalyticsservice.service;

import com.regulareedge.auditanalyticsservice.common.FeignResult;
import com.regulareedge.auditanalyticsservice.dto.response.CcoDashboardResponse;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.dto.response.RegulatoryReturnRefResponse;
import com.regulareedge.auditanalyticsservice.service.implementation.DashboardAggregationServiceImpl;
import com.regulareedge.auditanalyticsservice.service.interfaces.ReturnManagementQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardAggregationServiceImplTest {

    @Mock
    private ReturnManagementQueryService returnManagementQueryService;

    private DashboardAggregationServiceImpl dashboardAggregationService;

    @BeforeEach
    void setUp() {
        dashboardAggregationService = new DashboardAggregationServiceImpl(returnManagementQueryService);
    }

    @Test
    void getSummary_shouldAggregateCounts_whenUpstreamHealthy() {
        RegulatoryReturnRefResponse pendingReturn = new RegulatoryReturnRefResponse();
        pendingReturn.setReturnId(1);

        PenaltyProceedingRefResponse penalty = new PenaltyProceedingRefResponse();
        penalty.setPenaltyId(1);
        penalty.setPenaltyAmount(1500.0);

        when(returnManagementQueryService.getReturnsByStatus("PENDING_CCO_APPROVAL"))
                .thenReturn(FeignResult.ok(List.of(pendingReturn)));
        when(returnManagementQueryService.getPenaltiesByStatus("OPEN"))
                .thenReturn(FeignResult.ok(List.of(penalty)));

        CcoDashboardResponse response = dashboardAggregationService.getSummary();

        assertEquals(1, response.getPendingApprovals());
        assertEquals(1, response.getOpenPenalties());
        assertEquals(1500.0, response.getTotalPenaltyAmount());
        assertFalse(response.isDegraded());
    }

    @Test
    void getSummary_shouldFlagDegraded_whenReturnsCallFallsBack() {
        when(returnManagementQueryService.getReturnsByStatus("PENDING_CCO_APPROVAL"))
                .thenReturn(FeignResult.degraded(List.of()));
        when(returnManagementQueryService.getPenaltiesByStatus("OPEN"))
                .thenReturn(FeignResult.ok(List.of()));

        CcoDashboardResponse response = dashboardAggregationService.getSummary();

        assertEquals(0, response.getPendingApprovals());
        assertTrue(response.isDegraded());
    }

    @Test
    void getSummary_shouldFlagDegraded_whenPenaltiesCallFallsBack() {
        when(returnManagementQueryService.getReturnsByStatus("PENDING_CCO_APPROVAL"))
                .thenReturn(FeignResult.ok(List.of()));
        when(returnManagementQueryService.getPenaltiesByStatus("OPEN"))
                .thenReturn(FeignResult.degraded(List.of()));

        CcoDashboardResponse response = dashboardAggregationService.getSummary();

        assertEquals(0, response.getOpenPenalties());
        assertTrue(response.isDegraded());
    }

    @Test
    void getAllPenalties_shouldReturnDataFromQueryService() {
        PenaltyProceedingRefResponse penalty = new PenaltyProceedingRefResponse();
        penalty.setPenaltyId(9);
        when(returnManagementQueryService.getAllPenalties()).thenReturn(FeignResult.ok(List.of(penalty)));

        List<PenaltyProceedingRefResponse> penalties = dashboardAggregationService.getAllPenalties();

        assertEquals(1, penalties.size());
        assertEquals(9, penalties.get(0).getPenaltyId());
    }
}
