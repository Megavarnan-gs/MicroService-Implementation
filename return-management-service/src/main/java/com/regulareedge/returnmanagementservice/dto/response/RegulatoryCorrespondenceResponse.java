package com.regulareedge.returnmanagementservice.dto.response;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceType;

import java.time.LocalDate;

public class RegulatoryCorrespondenceResponse {

    private int correspondenceId;
    private int regulatorId;
    private CorrespondenceType type;
    private LocalDate receivedDate;
    private String subject;
    private LocalDate responseDueDate;
    private int assignedToId;
    private CorrespondenceStatus status;

    public RegulatoryCorrespondenceResponse() {
    }

    public RegulatoryCorrespondenceResponse(int correspondenceId, int regulatorId, CorrespondenceType type,
                                             LocalDate receivedDate, String subject, LocalDate responseDueDate,
                                             int assignedToId, CorrespondenceStatus status) {
        this.correspondenceId = correspondenceId;
        this.regulatorId = regulatorId;
        this.type = type;
        this.receivedDate = receivedDate;
        this.subject = subject;
        this.responseDueDate = responseDueDate;
        this.assignedToId = assignedToId;
        this.status = status;
    }

    public int getCorrespondenceId() {
        return correspondenceId;
    }

    public void setCorrespondenceId(int correspondenceId) {
        this.correspondenceId = correspondenceId;
    }

    public int getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(int regulatorId) {
        this.regulatorId = regulatorId;
    }

    public CorrespondenceType getType() {
        return type;
    }

    public void setType(CorrespondenceType type) {
        this.type = type;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getResponseDueDate() {
        return responseDueDate;
    }

    public void setResponseDueDate(LocalDate responseDueDate) {
        this.responseDueDate = responseDueDate;
    }

    public int getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(int assignedToId) {
        this.assignedToId = assignedToId;
    }

    public CorrespondenceStatus getStatus() {
        return status;
    }

    public void setStatus(CorrespondenceStatus status) {
        this.status = status;
    }
}
