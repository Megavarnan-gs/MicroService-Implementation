package com.regulareedge.compliancecoreservice.dto.response;

import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;

public class RegulatorResponse {

    private int regulatorId;
    private String name;
    private String jurisdiction;
    private String regulatoryDomain;
    private String contactDetails;
    private RegulatorStatus status;

    public RegulatorResponse() {
    }

    public RegulatorResponse(int regulatorId, String name, String jurisdiction, String regulatoryDomain,
                              String contactDetails, RegulatorStatus status) {
        this.regulatorId = regulatorId;
        this.name = name;
        this.jurisdiction = jurisdiction;
        this.regulatoryDomain = regulatoryDomain;
        this.contactDetails = contactDetails;
        this.status = status;
    }

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
