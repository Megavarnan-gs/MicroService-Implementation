package com.regulareedge.notificationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regulareedge.notificationservice.common.enums.NotificationCategory;
import com.regulareedge.notificationservice.common.enums.NotificationStatus;
import com.regulareedge.notificationservice.dto.request.CreateNotificationRequest;
import com.regulareedge.notificationservice.dto.response.NotificationResponse;
import com.regulareedge.notificationservice.security.JwtAuthenticationEntryPoint;
import com.regulareedge.notificationservice.security.JwtAuthenticationFilter;
import com.regulareedge.notificationservice.security.JwtUtil;
import com.regulareedge.notificationservice.service.interfaces.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Test
    @WithMockUser
    void getMyNotifications_shouldReturnPagedResults() throws Exception {
        NotificationResponse response = new NotificationResponse(
                1, 10, "Some message", NotificationCategory.RETURN, NotificationStatus.UNREAD,
                LocalDateTime.of(2026, 7, 16, 9, 0));
        Page<NotificationResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);

        when(notificationService.getMyNotifications(anyInt(), any())).thenReturn(page);

        mockMvc.perform(get("/notifications/my/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(10));
    }

    @Test
    @WithMockUser
    void create_shouldReturnCreated_whenRequestIsValid() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest();
        request.setUserId(10);
        request.setMessage("New data request: KYC documents. Please upload the data.");
        request.setCategory(NotificationCategory.DATA_COLLECTION);

        NotificationResponse response = new NotificationResponse(
                1, 10, request.getMessage(), NotificationCategory.DATA_COLLECTION, NotificationStatus.UNREAD,
                LocalDateTime.of(2026, 7, 16, 9, 0));

        when(notificationService.create(any(CreateNotificationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.notificationId").value(1))
                .andExpect(jsonPath("$.status").value("UNREAD"));
    }

    @Test
    @WithMockUser
    void create_shouldReturnBadRequest_whenPayloadInvalid() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest();

        mockMvc.perform(post("/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void markRead_shouldReturnUpdatedNotification() throws Exception {
        NotificationResponse response = new NotificationResponse(
                1, 10, "Some message", NotificationCategory.RETURN, NotificationStatus.READ,
                LocalDateTime.of(2026, 7, 16, 9, 0));

        when(notificationService.markRead(1)).thenReturn(response);

        mockMvc.perform(put("/notifications/markRead/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READ"));
    }
}
