package com.regulareedge.returnmanagementservice.entity;

import com.regulareedge.returnmanagementservice.common.enums.SubmissionStatus;
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

import java.time.LocalDateTime;

@Entity
@Table(name = "submission_record")
public class SubmissionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int submissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private RegulatoryReturn regulatoryReturn;

    /** External user id owned by auth-service - plain int, no JPA relation. */
    private int submittedById;

    private LocalDateTime submissionDateTime;

    private String submissionMode;

    private String acknowledgementRef;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public RegulatoryReturn getRegulatoryReturn() {
        return regulatoryReturn;
    }

    public void setRegulatoryReturn(RegulatoryReturn regulatoryReturn) {
        this.regulatoryReturn = regulatoryReturn;
    }

    /** Convenience accessor delegating to the associated RegulatoryReturn, for DTO mapping. */
    public int getReturnId() {
        return regulatoryReturn != null ? regulatoryReturn.getReturnId() : 0;
    }

    public int getSubmittedById() {
        return submittedById;
    }

    public void setSubmittedById(int submittedById) {
        this.submittedById = submittedById;
    }

    public LocalDateTime getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(LocalDateTime submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public String getSubmissionMode() {
        return submissionMode;
    }

    public void setSubmissionMode(String submissionMode) {
        this.submissionMode = submissionMode;
    }

    public String getAcknowledgementRef() {
        return acknowledgementRef;
    }

    public void setAcknowledgementRef(String acknowledgementRef) {
        this.acknowledgementRef = acknowledgementRef;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }
}
