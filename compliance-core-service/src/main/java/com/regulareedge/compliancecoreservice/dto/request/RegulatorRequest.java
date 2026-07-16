package com.regulareedge.compliancecoreservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RegulatorRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "jurisdiction is required")
    private String jurisdiction;

    @NotBlank(message = "regulatoryDomain is required")
    private String regulatoryDomain;

    private String contactDetails;

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
}
