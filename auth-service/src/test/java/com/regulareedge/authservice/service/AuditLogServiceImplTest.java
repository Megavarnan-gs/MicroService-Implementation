package com.regulareedge.authservice.service;

import com.regulareedge.authservice.dto.response.AuditLogResponse;
import com.regulareedge.authservice.entity.AuditLog;
import com.regulareedge.authservice.repository.AuditLogRepository;
import com.regulareedge.authservice.service.implementation.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private AuditLogServiceImpl auditLogService;

    @BeforeEach
    void setUp() {
        auditLogService = new AuditLogServiceImpl(auditLogRepository);
    }

    @Test
    void log_shouldPersistAuditLogEntry() {
        auditLogService.log(1, "REGISTER", "User", 1);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void log_shouldNotThrow_whenRepositoryFails() {
        when(auditLogRepository.save(any(AuditLog.class))).thenThrow(new RuntimeException("db down"));

        assertDoesNotThrow(() -> auditLogService.log(1, "REGISTER", "User", 1));
    }

    @Test
    void search_shouldReturnFilteredPageOfResults() {
        AuditLog auditLog = new AuditLog(1, "LOGIN_SUCCESS", "User", 1, LocalDateTime.now());
        auditLog.setAuditId(10L);

        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> page = new PageImpl<>(List.of(auditLog), pageable, 1);

        when(auditLogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<AuditLogResponse> result = auditLogService.search(1, "User", "LOGIN_SUCCESS", null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("LOGIN_SUCCESS", result.getContent().get(0).getAction());
        assertEquals(1, result.getContent().get(0).getUserId());
    }
}
