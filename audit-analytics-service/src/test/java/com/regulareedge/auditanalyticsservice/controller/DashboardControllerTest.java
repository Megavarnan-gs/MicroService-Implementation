package com.regulareedge.auditanalyticsservice.controller;

import com.regulareedge.auditanalyticsservice.dto.response.CcoDashboardResponse;
import com.regulareedge.auditanalyticsservice.dto.response.PenaltyProceedingRefResponse;
import com.regulareedge.auditanalyticsservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.auditanalyticsservice.security.JwtAuthenticationFilter;
import com.regulareedge.auditanalyticsservice.security.JwtUtil;
import com.regulareedge.auditanalyticsservice.service.interfaces.DashboardAggregationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardAggregationService dashboardAggregationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser(roles = "CCO")
    void dashboard_shouldReturnSummary() throws Exception {
        CcoDashboardResponse response = new CcoDashboardResponse(3, 2, 4500.0, false);
        when(dashboardAggregationService.getSummary()).thenReturn(response);

        mockMvc.perform(get("/analytics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pendingApprovals").value(3))
                .andExpect(jsonPath("$.openPenalties").value(2))
                .andExpect(jsonPath("$.degraded").value(false));
    }

    @Test
    @WithMockUser(roles = "CCO")
    void dashboard_shouldReflectDegradedFlag_whenUpstreamFailed() throws Exception {
        CcoDashboardResponse response = new CcoDashboardResponse(0, 0, 0.0, true);
        when(dashboardAggregationService.getSummary()).thenReturn(response);

        mockMvc.perform(get("/analytics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.degraded").value(true));
    }

    @Test
    @WithMockUser(roles = "CCO")
    void allPenalties_shouldReturnPenalties() throws Exception {
        PenaltyProceedingRefResponse penalty = new PenaltyProceedingRefResponse();
        penalty.setPenaltyId(5);
        when(dashboardAggregationService.getAllPenalties()).thenReturn(List.of(penalty));

        mockMvc.perform(get("/analytics/all-penalties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].penaltyId").value(5));
    }
}
