package com.regulareedge.returnmanagementservice.entity;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
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

import java.time.LocalDate;

@Entity
@Table(name = "penalty_proceeding")
public class PenaltyProceeding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int penaltyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correspondence_id", nullable = false)
    private RegulatoryCorrespondence correspondence;

    /** External id owned by compliance-core-service - plain int, no JPA relation. */
    private int obligationId;

    private String penaltyType;

    private double penaltyAmount;

    private String disputeStatus;

    private LocalDate paidDate;

    @Enumerated(EnumType.STRING)
    private PenaltyStatus status;

    public int getPenaltyId() {
        return penaltyId;
    }

    public void setPenaltyId(int penaltyId) {
        this.penaltyId = penaltyId;
    }

    public RegulatoryCorrespondence getCorrespondence() {
        return correspondence;
    }

    public void setCorrespondence(RegulatoryCorrespondence correspondence) {
        this.correspondence = correspondence;
    }

    /** Convenience accessor delegating to the associated RegulatoryCorrespondence, for DTO mapping. */
    public int getCorrespondenceId() {
        return correspondence != null ? correspondence.getCorrespondenceId() : 0;
    }

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public String getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public PenaltyStatus getStatus() {
        return status;
    }

    public void setStatus(PenaltyStatus status) {
        this.status = status;
    }
}
