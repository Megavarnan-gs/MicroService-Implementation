package com.regulareedge.notificationservice.service.interfaces;

import com.regulareedge.notificationservice.dto.response.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AuditLogService {

    /**
     * Fail-open: persistence failures are logged (WARN) and swallowed so that the caller's
     * business operation is never interrupted or rolled back by an audit-logging failure.
     */
    void log(Integer userId, String action, String entityType, Integer recordId);

    Page<AuditLogResponse> search(Integer userId, String entityType, String action, LocalDateTime startDate,
                                   LocalDateTime endDate, Pageable pageable);
}
