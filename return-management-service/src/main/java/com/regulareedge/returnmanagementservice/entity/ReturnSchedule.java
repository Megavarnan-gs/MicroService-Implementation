package com.regulareedge.returnmanagementservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "return_schedule")
public class ReturnSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private RegulatoryReturn regulatoryReturn;

    private String scheduleName;

    @Column(columnDefinition = "TEXT")
    private String dataContent;

    /** External user id owned by auth-service - plain int, no JPA relation. */
    private int validatedById;

    private String status;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public int getValidatedById() {
        return validatedById;
    }

    public void setValidatedById(int validatedById) {
        this.validatedById = validatedById;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
