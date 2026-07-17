package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.dto.response.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AuditLogService {

    void log(Integer userId, String action, String entityType, Integer recordId);

    Page<AuditLogResponse> search(Integer userId, String entityType, String action, LocalDateTime startDate,
                                   LocalDateTime endDate, Pageable pageable);
}
