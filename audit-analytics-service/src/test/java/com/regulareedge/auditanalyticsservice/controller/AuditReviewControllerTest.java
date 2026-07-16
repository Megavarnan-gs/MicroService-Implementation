package com.regulareedge.auditanalyticsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.auditanalyticsservice.common.enums.AuditRating;
import com.regulareedge.auditanalyticsservice.common.enums.AuditReviewStatus;
import com.regulareedge.auditanalyticsservice.dto.request.AuditReviewRequest;
import com.regulareedge.auditanalyticsservice.dto.response.AuditReviewResponse;
import com.regulareedge.auditanalyticsservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.auditanalyticsservice.security.JwtAuthenticationFilter;
import com.regulareedge.auditanalyticsservice.security.JwtUtil;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditReviewService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuditReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuditReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditReviewService auditReviewService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser(roles = "AUDITOR")
    void add_shouldReturnCreated_whenRequestIsValid() throws Exception {
        AuditReviewRequest request = new AuditReviewRequest();
        request.setObligationId(1);
        request.setAuditorId(2);
        request.setReviewPeriod("2026-Q1");
        request.setScope("Full");
        request.setRating(AuditRating.SATISFACTORY);
        request.setReviewDate(LocalDate.of(2026, 3, 1));

        AuditReviewResponse response = new AuditReviewResponse(
                100, 1, 2, "2026-Q1", "Full", null, AuditRating.SATISFACTORY,
                LocalDate.of(2026, 3, 1), AuditReviewStatus.DRAFT);

        when(auditReviewService.add(any(AuditReviewRequest.class))).thenReturn(response);

        mockMvc.perform(post("/audit-reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId").value(100))
                .andExpect(jsonPath("$.status").value("DRAFT"));
    }

    @Test
    @WithMockUser(roles = "AUDITOR")
    void add_shouldReturnBadRequest_whenPayloadInvalid() throws Exception {
        AuditReviewRequest request = new AuditReviewRequest();

        mockMvc.perform(post("/audit-reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "AUDITOR")
    void myReviews_shouldReturnReviewsForAuditor() throws Exception {
        AuditReviewResponse response = new AuditReviewResponse(
                100, 1, 2, "2026-Q1", "Full", null, AuditRating.SATISFACTORY,
                LocalDate.of(2026, 3, 1), AuditReviewStatus.DRAFT);
        when(auditReviewService.getMyReviews(2)).thenReturn(List.of(response));

        mockMvc.perform(get("/audit-reviews/myReviews/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auditorId").value(2));
    }

    @Test
    @WithMockUser(roles = "CCO")
    void getAll_shouldReturnAllReviews() throws Exception {
        AuditReviewResponse response = new AuditReviewResponse(
                100, 1, 2, "2026-Q1", "Full", null, AuditRating.SATISFACTORY,
                LocalDate.of(2026, 3, 1), AuditReviewStatus.DRAFT);
        when(auditReviewService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/audit-reviews/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(100));
    }
}
