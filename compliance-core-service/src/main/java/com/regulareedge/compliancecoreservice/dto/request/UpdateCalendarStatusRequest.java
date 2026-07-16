package com.regulareedge.compliancecoreservice.dto.request;

import com.regulareedge.compliancecoreservice.common.enums.CalendarStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateCalendarStatusRequest {

    @NotNull(message = "calendarId is required")
    @Positive(message = "calendarId must be positive")
    private Integer calendarId;

    @NotNull(message = "status is required")
    private CalendarStatus status;

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public CalendarStatus getStatus() {
        return status;
    }

    public void setStatus(CalendarStatus status) {
        this.status = status;
    }
}
