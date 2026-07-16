package com.regulareedge.returnmanagementservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "regulatory_return")
public class RegulatoryReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int returnId;

    /** External id owned by compliance-core-service - plain int, no JPA relation. */
    private int obligationId;

    /** External id owned by compliance-core-service - plain int, no JPA relation. */
    private int calendarId;

    private String reportingPeriod;

    /** External user id owned by auth-service - plain int, no JPA relation. */
    private int preparedById;

    private int totalSchedules;

    private LocalDate submissionDeadline;

    /** status String NOT enum - this fixes the 500 error (matches monolith convention). */
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
