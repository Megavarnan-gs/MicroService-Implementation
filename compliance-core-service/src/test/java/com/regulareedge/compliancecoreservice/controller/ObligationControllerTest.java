package com.regulareedge.compliancecoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.compliancecoreservice.common.enums.Frequency;
import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;
import com.regulareedge.compliancecoreservice.dto.request.RegulatoryObligationRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateObligationStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatoryObligationResponse;
import com.regulareedge.compliancecoreservice.exception.InvalidRequestException;
import com.regulareedge.compliancecoreservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.compliancecoreservice.security.JwtAuthenticationFilter;
import com.regulareedge.compliancecoreservice.security.JwtUtil;
import com.regulareedge.compliancecoreservice.service.interfaces.ObligationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ObligationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ObligationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ObligationService obligationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser(roles = "ADMIN")
    void add_shouldReturnCreated_whenRequestIsValid() throws Exception {
        RegulatoryObligationRequest request = new RegulatoryObligationRequest();
        request.setRegulatorId(1);
        request.setReturnName("Liquidity Return");
        request.setReturnCode("LR-01");
        request.setFrequency(Frequency.QUARTERLY);
        request.setOwnerId(5);

        RegulatoryObligationResponse response = new RegulatoryObligationResponse(
                10, 1, "Liquidity Return", "LR-01", Frequency.QUARTERLY, null, null, 5, ObligationStatus.ACTIVE);

        when(obligationService.create(any(RegulatoryObligationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/obligations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.returnName").value("Liquidity Return"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void add_shouldReturnBadRequest_whenPayloadInvalid() throws Exception {
        RegulatoryObligationRequest request = new RegulatoryObligationRequest();

        mockMvc.perform(post("/obligations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void add_shouldReturnBadRequest_whenRegulatorNotActive() throws Exception {
        RegulatoryObligationRequest request = new RegulatoryObligationRequest();
        request.setRegulatorId(2);
        request.setReturnName("Liquidity Return");
        request.setReturnCode("LR-01");
        request.setFrequency(Frequency.QUARTERLY);
        request.setOwnerId(5);

        when(obligationService.create(any(RegulatoryObligationRequest.class)))
                .thenThrow(new InvalidRequestException("Regulator with id 2 is not ACTIVE"));

        mockMvc.perform(post("/obligations/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_shouldReturnObligations() throws Exception {
        RegulatoryObligationResponse response = new RegulatoryObligationResponse(
                10, 1, "Liquidity Return", "LR-01", Frequency.QUARTERLY, null, null, 5, ObligationStatus.ACTIVE);
        when(obligationService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/obligations/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].returnCode").value("LR-01"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateStatus_shouldReturnUpdatedObligation() throws Exception {
        UpdateObligationStatusRequest request = new UpdateObligationStatusRequest();
        request.setObligationId(10);
        request.setStatus(ObligationStatus.ARCHIVED);

        RegulatoryObligationResponse response = new RegulatoryObligationResponse(
                10, 1, "Liquidity Return", "LR-01", Frequency.QUARTERLY, null, null, 5, ObligationStatus.ARCHIVED);

        when(obligationService.updateStatus(any(UpdateObligationStatusRequest.class))).thenReturn(response);

        mockMvc.perform(put("/obligations/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ARCHIVED"));
    }
}
