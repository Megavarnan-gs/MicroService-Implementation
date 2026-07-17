package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.dto.response.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AuditLogService {

    /**
     * Fail-open: persists an audit entry for a write action. Any persistence failure is
     * caught, logged at WARN, and never propagated - audit logging must never break the
     * caller's business operation.
     */
    void log(Integer userId, String action, String entityType, Integer recordId);

    Page<AuditLogResponse> search(Integer userId, String entityType, String action,
                                   LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
