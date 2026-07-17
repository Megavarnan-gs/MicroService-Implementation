package com.regulareedge.returnmanagementservice.dto.response;

import java.time.LocalDateTime;

public class AuditLogResponse {

    private Long auditId;
    private Integer userId;
    private String action;
    private String entityType;
    private Integer recordId;
    private LocalDateTime timestamp;

    public AuditLogResponse() {
    }

    public AuditLogResponse(Long auditId, Integer userId, String action, String entityType,
                             Integer recordId, LocalDateTime timestamp) {
        this.auditId = auditId;
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.recordId = recordId;
        this.timestamp = timestamp;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
