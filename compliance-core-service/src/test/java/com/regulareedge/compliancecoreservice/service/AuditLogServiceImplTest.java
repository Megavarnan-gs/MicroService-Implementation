package com.regulareedge.compliancecoreservice.service;

import com.regulareedge.compliancecoreservice.dto.response.AuditLogResponse;
import com.regulareedge.compliancecoreservice.entity.AuditLog;
import com.regulareedge.compliancecoreservice.repository.AuditLogRepository;
import com.regulareedge.compliancecoreservice.service.implementation.AuditLogServiceImpl;
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
    void log_shouldPersistAuditEntry() {
        auditLogService.log(5, "OBLIGATION_CREATED", "RegulatoryObligation", 10);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void log_shouldNotPropagateException_whenPersistenceFails() {
        when(auditLogRepository.save(any(AuditLog.class))).thenThrow(new RuntimeException("DB down"));

        auditLogService.log(5, "OBLIGATION_CREATED", "RegulatoryObligation", 10);

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void search_shouldFilterByUserId() {
        AuditLog auditLog = new AuditLog();
        auditLog.setAuditId(1L);
        auditLog.setUserId(5);
        auditLog.setAction("OBLIGATION_CREATED");
        auditLog.setEntityType("RegulatoryObligation");
        auditLog.setRecordId(10);
        auditLog.setTimestamp(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> page = new PageImpl<>(List.of(auditLog));

        when(auditLogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<AuditLogResponse> result = auditLogService.search(5, null, null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(5, result.getContent().get(0).getUserId());
    }
}
