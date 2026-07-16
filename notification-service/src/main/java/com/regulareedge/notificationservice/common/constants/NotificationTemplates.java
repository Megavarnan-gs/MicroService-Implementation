package com.regulareedge.notificationservice.common.constants;

/**
 * Message format fragments used by NotificationTemplateServiceImpl. Wording is ported
 * verbatim from the monolith's config.NotificationFactory helper (plain string
 * concatenation, not String.format) so downstream notification text stays consistent
 * with the previously shipped behaviour.
 */
public final class NotificationTemplates {

    private NotificationTemplates() {
    }

    public static final String DEADLINE_REMINDER_PREFIX = "Reminder: ";
    public static final String DEADLINE_REMINDER_MIDDLE = " is due on ";
    public static final String DEADLINE_REMINDER_SUFFIX = ". Please submit on time.";

    public static final String DATA_REQUEST_PREFIX = "New data request: ";
    public static final String DATA_REQUEST_SUFFIX = ". Please upload the data.";

    public static final String PENALTY_ALERT_PREFIX = "Alert: A penalty of Rs. ";
    public static final String PENALTY_ALERT_SUFFIX = " has been raised. Please take action.";

    public static final String CCO_APPROVAL_PREFIX = "Action Required: ";
    public static final String CCO_APPROVAL_SUFFIX = " is pending your approval.";
}
