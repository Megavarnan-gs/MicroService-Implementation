package com.regulareedge.returnmanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.returnmanagementservice.common.enums.ReturnStatus;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryReturnRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateReturnStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.CcoDashboardResponse;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryReturnResponse;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.returnmanagementservice.security.JwtAuthenticationFilter;
import com.regulareedge.returnmanagementservice.security.JwtUtil;
import com.regulareedge.returnmanagementservice.service.interfaces.DashboardService;
import com.regulareedge.returnmanagementservice.service.interfaces.RegulatoryReturnService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegulatoryReturnController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegulatoryReturnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegulatoryReturnService regulatoryReturnService;

    @MockBean
    private DashboardService dashboardService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser(roles = "CO")
    void create_shouldReturnCreated_whenRequestIsValid() throws Exception {
        RegulatoryReturnRequest request = new RegulatoryReturnRequest();
        request.setObligationId(10);
        request.setCalendarId(20);
        request.setReportingPeriod("Q1-2026");
        request.setPreparedById(5);
        request.setTotalSchedules(2);
        request.setSubmissionDeadline(LocalDate.of(2026, 3, 31));

        RegulatoryReturnResponse response = new RegulatoryReturnResponse(
                1, 10, 20, "Q1-2026", 5, 2, LocalDate.of(2026, 3, 31), ReturnStatus.DRAFT.name());

        when(regulatoryReturnService.create(any(RegulatoryReturnRequest.class))).thenReturn(response);

        mockMvc.perform(post("/returns/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportingPeriod").value("Q1-2026"))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    @WithMockUser(roles = "CO")
    void create_shouldReturnBadRequest_whenPayloadInvalid() throws Exception {
        RegulatoryReturnRequest request = new RegulatoryReturnRequest();

        mockMvc.perform(post("/returns/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CO")
    void create_shouldReturnBadRequest_whenObligationInvalid() throws Exception {
        RegulatoryReturnRequest request = new RegulatoryReturnRequest();
        request.setObligationId(99);
        request.setCalendarId(20);
        request.setReportingPeriod("Q1-2026");
        request.setPreparedById(5);
        request.setSubmissionDeadline(LocalDate.of(2026, 3, 31));

        when(regulatoryReturnService.create(any(RegulatoryReturnRequest.class)))
                .thenThrow(new InvalidRequestException("Obligation not found with id: 99"));

        mockMvc.perform(post("/returns/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CCO")
    void pendingApprovals_shouldReturnReturns() throws Exception {
        RegulatoryReturnResponse response = new RegulatoryReturnResponse(
                1, 10, 20, "Q1-2026", 5, 2, LocalDate.of(2026, 3, 31), ReturnStatus.PENDING_CCO_APPROVAL.name());
        when(regulatoryReturnService.getPendingApprovals()).thenReturn(List.of(response));

        mockMvc.perform(get("/returns/pendingApprovals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING_CCO_APPROVAL"));
    }

    @Test
    @WithMockUser(roles = "CCO")
    void dashboardSummary_shouldReturnSummary() throws Exception {
        when(dashboardService.getSummary()).thenReturn(new CcoDashboardResponse(2, 3, 15000.0));

        mockMvc.perform(get("/returns/dashboard-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pendingApprovals").value(2))
                .andExpect(jsonPath("$.openPenalties").value(3))
                .andExpect(jsonPath("$.totalPenaltyAmount").value(15000.0));
    }

    @Test
    @WithMockUser(roles = "CCO")
    void updateStatus_shouldReturnUpdatedReturn() throws Exception {
        UpdateReturnStatusRequest request = new UpdateReturnStatusRequest();
        request.setReturnId(1);
        request.setStatus(ReturnStatus.APPROVED.name());

        RegulatoryReturnResponse response = new RegulatoryReturnResponse(
                1, 10, 20, "Q1-2026", 5, 2, LocalDate.of(2026, 3, 31), ReturnStatus.APPROVED.name());

        when(regulatoryReturnService.updateStatus(any(UpdateReturnStatusRequest.class))).thenReturn(response);

        mockMvc.perform(put("/returns/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
