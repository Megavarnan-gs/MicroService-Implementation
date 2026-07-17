package com.regulareedge.authservice.mapper;

import com.regulareedge.authservice.dto.response.AuditLogResponse;
import com.regulareedge.authservice.entity.AuditLog;

public final class AuditLogMapper {

    private AuditLogMapper() {
    }

    public static AuditLogResponse toResponse(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.getAuditId(),
                auditLog.getUserId(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getRecordId(),
                auditLog.getTimestamp());
    }
}
