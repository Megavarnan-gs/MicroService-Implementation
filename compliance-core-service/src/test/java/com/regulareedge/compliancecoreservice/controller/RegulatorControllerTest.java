package com.regulareedge.compliancecoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import com.regulareedge.compliancecoreservice.dto.request.DeleteRegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.request.RegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatorResponse;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.compliancecoreservice.security.JwtAuthenticationFilter;
import com.regulareedge.compliancecoreservice.security.JwtUtil;
import com.regulareedge.compliancecoreservice.service.interfaces.RegulatorService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RegulatorController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegulatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegulatorService regulatorService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser(roles = "ADMIN")
    void add_shouldReturnCreated_whenRequestIsValid() throws Exception {
        RegulatorRequest request = new RegulatorRequest();
        request.setName("FCA");
        request.setJurisdiction("UK");
        request.setRegulatoryDomain("Banking");

        RegulatorResponse response = new RegulatorResponse(1, "FCA", "UK", "Banking", null, RegulatorStatus.ACTIVE);

        when(regulatorService.create(any(RegulatorRequest.class))).thenReturn(response);

        mockMvc.perform(post("/regulators/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("FCA"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void add_shouldReturnBadRequest_whenPayloadInvalid() throws Exception {
        RegulatorRequest request = new RegulatorRequest();

        mockMvc.perform(post("/regulators/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_shouldReturnRegulators() throws Exception {
        RegulatorResponse response = new RegulatorResponse(1, "FCA", "UK", "Banking", null, RegulatorStatus.ACTIVE);
        when(regulatorService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/regulators/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("FCA"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_shouldReturnOk_whenRegulatorExists() throws Exception {
        DeleteRegulatorRequest request = new DeleteRegulatorRequest();
        request.setRegulatorId(1);

        mockMvc.perform(delete("/regulators/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Regulator with id 1 has been deleted"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_shouldReturnNotFound_whenRegulatorMissing() throws Exception {
        DeleteRegulatorRequest request = new DeleteRegulatorRequest();
        request.setRegulatorId(99);

        doThrow(new ResourceNotFoundException("Regulator not found with id: 99"))
                .when(regulatorService).delete(99);

        mockMvc.perform(delete("/regulators/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
