package com.regulareedge.notificationservice.service;

import com.regulareedge.notificationservice.dto.response.AuditLogResponse;
import com.regulareedge.notificationservice.entity.AuditLog;
import com.regulareedge.notificationservice.repository.AuditLogRepository;
import com.regulareedge.notificationservice.service.implementation.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private AuditLogServiceImpl auditLogService;

    private AuditLog auditLog;

    @BeforeEach
    void setUp() {
        auditLogService = new AuditLogServiceImpl(auditLogRepository);

        auditLog = new AuditLog();
        auditLog.setAuditId(1L);
        auditLog.setUserId(10);
        auditLog.setAction("NOTIFICATION_CREATED");
        auditLog.setEntityType("Notification");
        auditLog.setRecordId(5);
        auditLog.setTimestamp(LocalDateTime.of(2026, 7, 16, 9, 0));
    }

    @Test
    void log_shouldPersistAuditLogEntry() {
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(auditLog);

        auditLogService.log(10, "NOTIFICATION_CREATED", "Notification", 5);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());

        assertEquals(10, captor.getValue().getUserId());
        assertEquals("NOTIFICATION_CREATED", captor.getValue().getAction());
        assertEquals("Notification", captor.getValue().getEntityType());
        assertEquals(5, captor.getValue().getRecordId());
    }

    @Test
    void log_shouldFailOpen_whenPersistenceThrows() {
        when(auditLogRepository.save(any(AuditLog.class))).thenThrow(new RuntimeException("db down"));

        assertDoesNotThrow(() -> auditLogService.log(10, "NOTIFICATION_CREATED", "Notification", 5));
    }

    @Test
    void search_shouldReturnMappedPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> page = new PageImpl<>(List.of(auditLog), pageable, 1);
        when(auditLogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<AuditLogResponse> result = auditLogService.search(10, "Notification", "NOTIFICATION_CREATED",
                LocalDateTime.of(2026, 7, 1, 0, 0), LocalDateTime.of(2026, 7, 31, 23, 59), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(10, result.getContent().get(0).getUserId());
        assertEquals("NOTIFICATION_CREATED", result.getContent().get(0).getAction());
    }

    @Test
    void search_shouldReturnEmptyPage_whenNoFiltersMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(auditLogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<AuditLogResponse> result = auditLogService.search(null, null, null, null, null, pageable);

        assertEquals(0, result.getTotalElements());
    }
}
