package com.regulareedge.auditanalyticsservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

/**
 * Slim projection of return-management-service's RegulatoryReturnResponse. Only the
 * fields needed by this service's aggregation/dashboard logic are declared; unknown
 * properties returned by the real endpoint are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegulatoryReturnRefResponse {

    private int returnId;
    private int obligationId;
    private int calendarId;
    private String reportingPeriod;
    private int preparedById;
    private int totalSchedules;
    private LocalDate submissionDeadline;
    private String status;

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public String getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(String reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public int getPreparedById() {
        return preparedById;
    }

    public void setPreparedById(int preparedById) {
        this.preparedById = preparedById;
    }

    public int getTotalSchedules() {
        return totalSchedules;
    }

    public void setTotalSchedules(int totalSchedules) {
        this.totalSchedules = totalSchedules;
    }

    public LocalDate getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(LocalDate submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
