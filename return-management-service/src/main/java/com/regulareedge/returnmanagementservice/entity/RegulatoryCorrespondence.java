package com.regulareedge.returnmanagementservice.entity;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "regulatory_correspondence")
public class RegulatoryCorrespondence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int correspondenceId;

    /** External id owned by compliance-core-service - plain int, no JPA relation. */
    private int regulatorId;

    @Enumerated(EnumType.STRING)
    private CorrespondenceType type;

    private LocalDate receivedDate;

    private String subject;

    private LocalDate responseDueDate;

    /** External user id owned by auth-service - plain int, no JPA relation. */
    private int assignedToId;

    @Enumerated(EnumType.STRING)
    private CorrespondenceStatus status;

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
