package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.AuditLogResponse;
import com.regulareedge.compliancecoreservice.entity.AuditLog;

public final class AuditLogMapper {

    private AuditLogMapper() {
    }

    public static AuditLogResponse toResponse(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }
        return new AuditLogResponse(
                auditLog.getAuditId(),
                auditLog.getUserId(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getRecordId(),
                auditLog.getTimestamp());
    }
}
