package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.ComplianceCalendarResponse;
import com.regulareedge.compliancecoreservice.entity.ComplianceCalendar;

public final class CalendarMapper {

    private CalendarMapper() {
    }

    public static ComplianceCalendarResponse toResponse(ComplianceCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return new ComplianceCalendarResponse(
                calendar.getCalendarId(),
                calendar.getObligationId(),
                calendar.getDueDate(),
                calendar.getDataCutOffDate(),
                calendar.getReminderDays(),
                calendar.getStatus());
    }
}
