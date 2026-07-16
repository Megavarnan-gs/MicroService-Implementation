package com.regulareedge.compliancecoreservice.entity;

import com.regulareedge.compliancecoreservice.common.enums.Frequency;
import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;
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

@Entity
@Table(name = "regulatory_obligation")
public class RegulatoryObligation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int obligationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regulator_id", nullable = false)
    private Regulator regulator;

    private String returnName;
    private String returnCode;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private String submissionMode;
    private String returnTemplateRef;

    /** External user id (owned by auth-service) - plain int, not a JPA relationship. */
    private int ownerId;

    @Enumerated(EnumType.STRING)
    private ObligationStatus status;

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public Regulator getRegulator() {
        return regulator;
    }

    public void setRegulator(Regulator regulator) {
        this.regulator = regulator;
    }

    /** Convenience accessor delegating to the associated Regulator, for simple DTO mapping. */
    public int getRegulatorId() {
        return regulator != null ? regulator.getRegulatorId() : 0;
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
