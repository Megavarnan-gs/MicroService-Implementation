package com.regulareedge.auditanalyticsservice.mapper;

import com.regulareedge.auditanalyticsservice.dto.response.AuditLogResponse;
import com.regulareedge.auditanalyticsservice.entity.AuditLog;

public final class AuditLogMapper {

    private AuditLogMapper() {
    }

    public static AuditLogResponse toResponse(AuditLog entity) {
        if (entity == null) {
            return null;
        }
        return new AuditLogResponse(
                entity.getAuditId(),
                entity.getUserId(),
                entity.getAction(),
                entity.getEntityType(),
                entity.getRecordId(),
                entity.getTimestamp());
    }
}
