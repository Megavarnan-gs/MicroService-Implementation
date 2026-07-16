package com.regulareedge.compliancecoreservice.dto.response;

import com.regulareedge.compliancecoreservice.common.enums.DataRequestStatus;

import java.time.LocalDate;

public class DataCollectionRequestResponse {

    private int requestId;
    private int calendarId;
    private int dataOwnerId;
    private String dataDescription;
    private LocalDate dataCutOffDate;
    private LocalDate submissionDeadline;
    private DataRequestStatus status;

    public DataCollectionRequestResponse() {
    }

    public DataCollectionRequestResponse(int requestId, int calendarId, int dataOwnerId, String dataDescription,
                                          LocalDate dataCutOffDate, LocalDate submissionDeadline,
                                          DataRequestStatus status) {
        this.requestId = requestId;
        this.calendarId = calendarId;
        this.dataOwnerId = dataOwnerId;
        this.dataDescription = dataDescription;
        this.dataCutOffDate = dataCutOffDate;
        this.submissionDeadline = submissionDeadline;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public int getDataOwnerId() {
        return dataOwnerId;
    }

    public void setDataOwnerId(int dataOwnerId) {
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

    public DataRequestStatus getStatus() {
        return status;
    }

    public void setStatus(DataRequestStatus status) {
        this.status = status;
    }
}
