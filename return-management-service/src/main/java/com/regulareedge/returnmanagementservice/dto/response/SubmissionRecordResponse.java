package com.regulareedge.returnmanagementservice.dto.response;

import com.regulareedge.returnmanagementservice.common.enums.SubmissionStatus;

import java.time.LocalDateTime;

public class SubmissionRecordResponse {

    private int submissionId;
    private int returnId;
    private int submittedById;
    private LocalDateTime submissionDateTime;
    private String submissionMode;
    private String acknowledgementRef;
    private SubmissionStatus status;

    public SubmissionRecordResponse() {
    }

    public SubmissionRecordResponse(int submissionId, int returnId, int submittedById,
                                     LocalDateTime submissionDateTime, String submissionMode,
                                     String acknowledgementRef, SubmissionStatus status) {
        this.submissionId = submissionId;
        this.returnId = returnId;
        this.submittedById = submittedById;
        this.submissionDateTime = submissionDateTime;
        this.submissionMode = submissionMode;
        this.acknowledgementRef = acknowledgementRef;
        this.status = status;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getReturnId() {
        return returnId;
    }

    public void setReturnId(int returnId) {
        this.returnId = returnId;
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
