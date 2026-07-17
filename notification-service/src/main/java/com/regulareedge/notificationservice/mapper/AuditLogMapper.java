package com.regulareedge.notificationservice.mapper;

import com.regulareedge.notificationservice.dto.response.AuditLogResponse;
import com.regulareedge.notificationservice.entity.AuditLog;

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
