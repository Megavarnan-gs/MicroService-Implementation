package com.regulareedge.returnmanagementservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Small supporting request for CorrespondenceController's PUT /assign endpoint -
 * reassigns an existing RegulatoryCorrespondence to a different owner (assignedToId).
 */
public class AssignCorrespondenceRequest {

    @NotNull(message = "correspondenceId is required")
    @Positive(message = "correspondenceId must be positive")
    private Integer correspondenceId;

    @NotNull(message = "assignedToId is required")
    @Positive(message = "assignedToId must be positive")
    private Integer assignedToId;

    public Integer getCorrespondenceId() {
        return correspondenceId;
    }

    public void setCorrespondenceId(Integer correspondenceId) {
        this.correspondenceId = correspondenceId;
    }

    public Integer getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Integer assignedToId) {
        this.assignedToId = assignedToId;
    }
}
