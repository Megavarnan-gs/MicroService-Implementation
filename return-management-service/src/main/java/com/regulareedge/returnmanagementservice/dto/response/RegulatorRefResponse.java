package com.regulareedge.returnmanagementservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Slim projection of compliance-core-service's RegulatorResponse. Only the fields needed
 * to validate a regulatorId reference are declared; unknown properties returned by the
 * real endpoint are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegulatorRefResponse {

    private int regulatorId;
    private String name;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
