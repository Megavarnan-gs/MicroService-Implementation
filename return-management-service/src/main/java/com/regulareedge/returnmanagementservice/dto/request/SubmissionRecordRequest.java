package com.regulareedge.returnmanagementservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SubmissionRecordRequest {

    @NotNull(message = "returnId is required")
    @Positive(message = "returnId must be positive")
    private Integer returnId;

    @NotNull(message = "submittedById is required")
    @Positive(message = "submittedById must be positive")
    private Integer submittedById;

    @NotBlank(message = "submissionMode is required")
    private String submissionMode;

    private String acknowledgementRef;

    public Integer getReturnId() {
        return returnId;
    }

    public void setReturnId(Integer returnId) {
        this.returnId = returnId;
    }

    public Integer getSubmittedById() {
        return submittedById;
    }

    public void setSubmittedById(Integer submittedById) {
        this.submittedById = submittedById;
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
}
