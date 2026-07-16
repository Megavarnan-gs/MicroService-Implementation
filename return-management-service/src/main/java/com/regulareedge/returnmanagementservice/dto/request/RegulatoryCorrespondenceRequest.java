package com.regulareedge.returnmanagementservice.dto.request;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class RegulatoryCorrespondenceRequest {

    @NotNull(message = "regulatorId is required")
    @Positive(message = "regulatorId must be positive")
    private Integer regulatorId;

    @NotNull(message = "type is required")
    private CorrespondenceType type;

    @NotNull(message = "receivedDate is required")
    private LocalDate receivedDate;

    @NotBlank(message = "subject is required")
    private String subject;

    private LocalDate responseDueDate;

    @NotNull(message = "assignedToId is required")
    @Positive(message = "assignedToId must be positive")
    private Integer assignedToId;

    public Integer getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(Integer regulatorId) {
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

    public Integer getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Integer assignedToId) {
        this.assignedToId = assignedToId;
    }
}
