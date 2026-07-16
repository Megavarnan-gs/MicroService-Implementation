package com.regulareedge.notificationservice.service.implementation;

import com.regulareedge.notificationservice.common.constants.NotificationTemplates;
import com.regulareedge.notificationservice.service.interfaces.NotificationTemplateService;
import org.springframework.stereotype.Service;

@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    @Override
    public String deadlineReminder(String returnName, String date) {
        return NotificationTemplates.DEADLINE_REMINDER_PREFIX + returnName
                + NotificationTemplates.DEADLINE_REMINDER_MIDDLE + date
                + NotificationTemplates.DEADLINE_REMINDER_SUFFIX;
    }

    @Override
    public String dataRequestMessage(String dataDescription) {
        return NotificationTemplates.DATA_REQUEST_PREFIX + dataDescription
                + NotificationTemplates.DATA_REQUEST_SUFFIX;
    }

    @Override
    public String penaltyAlert(double amount) {
        return NotificationTemplates.PENALTY_ALERT_PREFIX + amount
                + NotificationTemplates.PENALTY_ALERT_SUFFIX;
    }

    @Override
    public String ccoApprovalRequest(String returnName) {
        return NotificationTemplates.CCO_APPROVAL_PREFIX + returnName
                + NotificationTemplates.CCO_APPROVAL_SUFFIX;
    }
}
