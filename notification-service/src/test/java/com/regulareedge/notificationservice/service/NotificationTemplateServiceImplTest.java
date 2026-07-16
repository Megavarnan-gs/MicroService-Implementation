package com.regulareedge.notificationservice.service;

import com.regulareedge.notificationservice.service.implementation.NotificationTemplateServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationTemplateServiceImplTest {

    private final NotificationTemplateServiceImpl templateService = new NotificationTemplateServiceImpl();

    @Test
    void deadlineReminder_shouldMatchMonolithWording() {
        String result = templateService.deadlineReminder("Annual Return", "2026-08-01");
        assertEquals("Reminder: Annual Return is due on 2026-08-01. Please submit on time.", result);
    }

    @Test
    void dataRequestMessage_shouldMatchMonolithWording() {
        String result = templateService.dataRequestMessage("KYC documents");
        assertEquals("New data request: KYC documents. Please upload the data.", result);
    }

    @Test
    void penaltyAlert_shouldMatchMonolithWording() {
        String result = templateService.penaltyAlert(5000.0);
        assertEquals("Alert: A penalty of Rs. 5000.0 has been raised. Please take action.", result);
    }

    @Test
    void ccoApprovalRequest_shouldMatchMonolithWording() {
        String result = templateService.ccoApprovalRequest("Quarterly Compliance Return");
        assertEquals("Action Required: Quarterly Compliance Return is pending your approval.", result);
    }
}
