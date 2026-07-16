package com.regulareedge.returnmanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public class RegulatoryReturnRequest {

    @NotNull(message = "obligationId is required")
    @Positive(message = "obligationId must be positive")
    private Integer obligationId;

    @NotNull(message = "calendarId is required")
    @Positive(message = "calendarId must be positive")
    private Integer calendarId;

    @NotBlank(message = "reportingPeriod is required")
    private String reportingPeriod;

    @NotNull(message = "preparedById is required")
    @Positive(message = "preparedById must be positive")
    private Integer preparedById;

    @PositiveOrZero(message = "totalSchedules must not be negative")
    private int totalSchedules;

    @NotNull(message = "submissionDeadline is required")
    private LocalDate submissionDeadline;

    public Integer getObligationId() {
        return obligationId;
    }

    public void setObligationId(Integer obligationId) {
        this.obligationId = obligationId;
    }

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public String getReportingPeriod() {
        return reportingPeriod;
    }

    public void setReportingPeriod(String reportingPeriod) {
        this.reportingPeriod = reportingPeriod;
    }

    public Integer getPreparedById() {
        return preparedById;
    }

    public void setPreparedById(Integer preparedById) {
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
}
