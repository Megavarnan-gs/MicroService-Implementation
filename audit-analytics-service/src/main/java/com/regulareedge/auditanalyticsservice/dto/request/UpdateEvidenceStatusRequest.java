package com.regulareedge.auditanalyticsservice.dto.request;

import com.regulareedge.auditanalyticsservice.common.enums.EvidenceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateEvidenceStatusRequest {

    @Positive
    private int evidenceId;

    @NotNull
    private EvidenceStatus status;

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public EvidenceStatus getStatus() {
        return status;
    }

    public void setStatus(EvidenceStatus status) {
        this.status = status;
    }
}
