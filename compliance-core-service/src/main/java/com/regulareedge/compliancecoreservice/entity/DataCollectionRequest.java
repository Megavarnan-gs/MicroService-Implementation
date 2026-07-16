package com.regulareedge.compliancecoreservice.entity;

import com.regulareedge.compliancecoreservice.common.enums.DataRequestStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "data_collection_request")
public class DataCollectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private ComplianceCalendar calendar;

    /** External user id (owned by auth-service) - plain int, not a JPA relationship. */
    private int dataOwnerId;

    private String dataDescription;
    private LocalDate dataCutOffDate;
    private LocalDate submissionDeadline;

    @Enumerated(EnumType.STRING)
    private DataRequestStatus status;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public ComplianceCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(ComplianceCalendar calendar) {
        this.calendar = calendar;
    }

    /** Convenience accessor delegating to the associated ComplianceCalendar. */
    public int getCalendarId() {
        return calendar != null ? calendar.getCalendarId() : 0;
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
