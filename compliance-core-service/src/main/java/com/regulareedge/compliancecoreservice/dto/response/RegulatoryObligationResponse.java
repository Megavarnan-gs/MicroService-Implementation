package com.regulareedge.compliancecoreservice.dto.response;

import com.regulareedge.compliancecoreservice.common.enums.Frequency;
import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;

public class RegulatoryObligationResponse {

    private int obligationId;
    private int regulatorId;
    private String returnName;
    private String returnCode;
    private Frequency frequency;
    private String submissionMode;
    private String returnTemplateRef;
    private int ownerId;
    private ObligationStatus status;

    public RegulatoryObligationResponse() {
    }

    public RegulatoryObligationResponse(int obligationId, int regulatorId, String returnName, String returnCode,
                                         Frequency frequency, String submissionMode, String returnTemplateRef,
                                         int ownerId, ObligationStatus status) {
        this.obligationId = obligationId;
        this.regulatorId = regulatorId;
        this.returnName = returnName;
        this.returnCode = returnCode;
        this.frequency = frequency;
        this.submissionMode = submissionMode;
        this.returnTemplateRef = returnTemplateRef;
        this.ownerId = ownerId;
        this.status = status;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public int getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(int regulatorId) {
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ObligationStatus getStatus() {
        return status;
    }

    public void setStatus(ObligationStatus status) {
        this.status = status;
    }
}
