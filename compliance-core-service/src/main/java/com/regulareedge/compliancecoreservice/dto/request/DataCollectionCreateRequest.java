package com.regulareedge.compliancecoreservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class DataCollectionCreateRequest {

    @NotNull(message = "calendarId is required")
    @Positive(message = "calendarId must be positive")
    private Integer calendarId;

    @NotNull(message = "dataOwnerId is required")
    @Positive(message = "dataOwnerId must be positive")
    private Integer dataOwnerId;

    @NotBlank(message = "dataDescription is required")
    private String dataDescription;

    @NotNull(message = "dataCutOffDate is required")
    private LocalDate dataCutOffDate;

    @NotNull(message = "submissionDeadline is required")
    private LocalDate submissionDeadline;

    public Integer getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    public Integer getDataOwnerId() {
        return dataOwnerId;
    }

    public void setDataOwnerId(Integer dataOwnerId) {
        this.dataOwnerId = dataOwnerId;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }

    public LocalDate getDataCutOffDate() {
        return dataCutOffDate;
    }

    public void setDataCutOffDate(LocalDate dataCutOffDate) {
        this.dataCutOffDate = dataCutOffDate;
    }

    public LocalDate getSubmissionDeadline() {
        return submissionDeadline;
    }

    public void setSubmissionDeadline(LocalDate submissionDeadline) {
        this.submissionDeadline = submissionDeadline;
    }
}
