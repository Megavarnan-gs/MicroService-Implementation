package com.regulareedge.returnmanagementservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Slim projection of compliance-core-service's RegulatoryObligationResponse. Only the
 * fields needed to validate an obligationId reference are declared; unknown properties
 * returned by the real endpoint are ignored.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObligationRefResponse {

    private int obligationId;
    private String status;

    public int getObligationId() {
        return obligationId;
    }

    public void setObligationId(int obligationId) {
        this.obligationId = obligationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
