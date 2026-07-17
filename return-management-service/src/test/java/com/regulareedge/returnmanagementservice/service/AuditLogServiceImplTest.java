package com.regulareedge.returnmanagementservice.service;

import com.regulareedge.returnmanagementservice.dto.response.AuditLogResponse;
import com.regulareedge.returnmanagementservice.entity.AuditLog;
import com.regulareedge.returnmanagementservice.repository.AuditLogRepository;
import com.regulareedge.returnmanagementservice.service.implementation.AuditLogServiceImpl;
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

    @BeforeEach
    void setUp() {
        auditLogService = new AuditLogServiceImpl(auditLogRepository);
    }

    @Test
    void log_shouldPersistAuditEntry_whenSaveSucceeds() {
        auditLogService.log(5, "RETURN_CREATED", "RegulatoryReturn", 1);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void log_shouldNotPropagate_whenRepositoryThrows() {
        when(auditLogRepository.save(any(AuditLog.class))).thenThrow(new RuntimeException("db down"));

        assertDoesNotThrow(() -> auditLogService.log(5, "RETURN_CREATED", "RegulatoryReturn", 1));
    }

    @Test
    void search_shouldReturnMappedPage() {
        AuditLog auditLog = new AuditLog();
        auditLog.setAuditId(1L);
        auditLog.setUserId(5);
        auditLog.setAction("RETURN_CREATED");
        auditLog.setEntityType("RegulatoryReturn");
        auditLog.setRecordId(1);
        auditLog.setTimestamp(LocalDateTime.of(2026, 1, 1, 10, 0));

        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> page = new PageImpl<>(List.of(auditLog), pageable, 1);

        when(auditLogRepository.findAll(org.mockito.ArgumentMatchers.<Specification<AuditLog>>any(),
                any(Pageable.class))).thenReturn(page);

        Page<AuditLogResponse> result = auditLogService.search(5, "RegulatoryReturn", "RETURN_CREATED",
                null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("RETURN_CREATED", result.getContent().get(0).getAction());
    }
}
