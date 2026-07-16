package com.regulareedge.compliancecoreservice.dto.request;

import com.regulareedge.compliancecoreservice.common.enums.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RegulatoryObligationRequest {

    @NotNull(message = "regulatorId is required")
    @Positive(message = "regulatorId must be positive")
    private Integer regulatorId;

    @NotBlank(message = "returnName is required")
    private String returnName;

    @NotBlank(message = "returnCode is required")
    private String returnCode;

    @NotNull(message = "frequency is required")
    private Frequency frequency;

    private String submissionMode;
    private String returnTemplateRef;

    @NotNull(message = "ownerId is required")
    @Positive(message = "ownerId must be positive")
    private Integer ownerId;

    public Integer getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(Integer regulatorId) {
        this.regulatorId = regulatorId;
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String getSubmissionMode() {
        return submissionMode;
    }

    public void setSubmissionMode(String submissionMode) {
        this.submissionMode = submissionMode;
    }

    public String getReturnTemplateRef() {
        return returnTemplateRef;
    }

    public void setReturnTemplateRef(String returnTemplateRef) {
        this.returnTemplateRef = returnTemplateRef;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}
