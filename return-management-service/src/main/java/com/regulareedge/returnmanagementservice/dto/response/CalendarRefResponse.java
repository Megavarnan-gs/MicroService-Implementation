package com.regulareedge.returnmanagementservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Slim projection of compliance-core-service's ComplianceCalendarResponse. Only the
 * fields needed to validate a calendarId reference are declared; unknown properties
 * returned by the real endpoint are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalendarRefResponse {

    private int calendarId;
    private int obligationId;
    private String status;

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
