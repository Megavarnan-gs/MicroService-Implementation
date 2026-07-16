package com.regulareedge.notificationservice.service.interfaces;

/**
 * Injectable, testable replacement for the monolith's static config.NotificationFactory
 * helper. Produces the exact same message text as the monolith so notification content
 * stays consistent, while remaining mockable in unit tests (unlike a static utility).
 */
public interface NotificationTemplateService {

    String deadlineReminder(String returnName, String date);

    String dataRequestMessage(String dataDescription);

    String penaltyAlert(double amount);

    String ccoApprovalRequest(String returnName);
}
