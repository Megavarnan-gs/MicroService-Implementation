package com.regulareedge.auditanalyticsservice.mapper;

import com.regulareedge.auditanalyticsservice.dto.response.ControlEvidenceResponse;
import com.regulareedge.auditanalyticsservice.entity.ControlEvidence;

public final class ControlEvidenceMapper {

    private ControlEvidenceMapper() {
    }

    public static ControlEvidenceResponse toResponse(ControlEvidence entity) {
        if (entity == null) {
            return null;
        }
        return new ControlEvidenceResponse(
                entity.getEvidenceId(),
                entity.getReturnId(),
                entity.getEvidenceType(),
                entity.getFilePath(),
                entity.getUploadedById(),
                entity.getUploadedDate(),
                entity.getStatus());
    }
}
