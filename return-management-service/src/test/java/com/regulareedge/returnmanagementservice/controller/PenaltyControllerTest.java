package com.regulareedge.returnmanagementservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import com.regulareedge.returnmanagementservice.dto.request.PenaltyProceedingRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdatePenaltyStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.PenaltyProceedingResponse;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.returnmanagementservice.security.JwtAuthenticationFilter;
import com.regulareedge.returnmanagementservice.security.JwtUtil;
import com.regulareedge.returnmanagementservice.service.interfaces.PenaltyService;
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

@WebMvcTest(controllers = PenaltyController.class)
@AutoConfigureMockMvc(addFilters = false)
class PenaltyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PenaltyService penaltyService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser(roles = "LEGAL")
    void add_shouldReturnCreated_whenRequestIsValid() throws Exception {
        PenaltyProceedingRequest request = new PenaltyProceedingRequest();
        request.setCorrespondenceId(1);
        request.setObligationId(10);
        request.setPenaltyType("Late Filing");
        request.setPenaltyAmount(5000.0);

        PenaltyProceedingResponse response = new PenaltyProceedingResponse(
                100, 1, 10, "Late Filing", 5000.0, null, null, PenaltyStatus.OPEN);

        when(penaltyService.add(any(PenaltyProceedingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/penalties/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.penaltyType").value("Late Filing"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    @WithMockUser(roles = "LEGAL")
    void add_shouldReturnBadRequest_whenPayloadInvalid() throws Exception {
        PenaltyProceedingRequest request = new PenaltyProceedingRequest();

        mockMvc.perform(post("/penalties/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CCO")
    void getAll_shouldReturnPenalties() throws Exception {
        PenaltyProceedingResponse response = new PenaltyProceedingResponse(
                100, 1, 10, "Late Filing", 5000.0, null, null, PenaltyStatus.OPEN);
        when(penaltyService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/penalties/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].penaltyId").value(100));
    }

    @Test
    @WithMockUser(roles = "LEGAL")
    void updateStatus_shouldReturnUpdatedPenalty() throws Exception {
        UpdatePenaltyStatusRequest request = new UpdatePenaltyStatusRequest();
        request.setPenaltyId(100);
        request.setStatus(PenaltyStatus.PAID);

        PenaltyProceedingResponse response = new PenaltyProceedingResponse(
                100, 1, 10, "Late Filing", 5000.0, null, null, PenaltyStatus.PAID);

        when(penaltyService.updateStatus(any(UpdatePenaltyStatusRequest.class))).thenReturn(response);

        mockMvc.perform(put("/penalties/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @WithMockUser(roles = "LEGAL")
    void updateStatus_shouldReturnNotFound_whenPenaltyMissing() throws Exception {
        UpdatePenaltyStatusRequest request = new UpdatePenaltyStatusRequest();
        request.setPenaltyId(404);
        request.setStatus(PenaltyStatus.PAID);

        when(penaltyService.updateStatus(any(UpdatePenaltyStatusRequest.class)))
                .thenThrow(new ResourceNotFoundException("Penalty not found with id: 404"));

        mockMvc.perform(put("/penalties/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
