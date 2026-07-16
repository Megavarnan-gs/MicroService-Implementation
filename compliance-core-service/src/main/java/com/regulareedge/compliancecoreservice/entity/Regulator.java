package com.regulareedge.compliancecoreservice.entity;

import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "regulator")
public class Regulator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int regulatorId;

    private String name;
    private String jurisdiction;
    private String regulatoryDomain;
    private String contactDetails;

    @Enumerated(EnumType.STRING)
    private RegulatorStatus status;

    public int getRegulatorId() {
        return regulatorId;
    }

    public void setRegulatorId(int regulatorId) {
        this.regulatorId = regulatorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getRegulatoryDomain() {
        return regulatoryDomain;
    }

    public void setRegulatoryDomain(String regulatoryDomain) {
        this.regulatoryDomain = regulatoryDomain;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public RegulatorStatus getStatus() {
        return status;
    }

    public void setStatus(RegulatorStatus status) {
        this.status = status;
    }
}
