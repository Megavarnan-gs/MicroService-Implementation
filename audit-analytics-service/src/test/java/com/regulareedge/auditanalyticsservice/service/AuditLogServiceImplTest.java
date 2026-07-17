package com.regulareedge.auditanalyticsservice.service;

import com.regulareedge.auditanalyticsservice.dto.response.AuditLogResponse;
import com.regulareedge.auditanalyticsservice.entity.AuditLog;
import com.regulareedge.auditanalyticsservice.repository.AuditLogRepository;
import com.regulareedge.auditanalyticsservice.service.implementation.AuditLogServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void log_shouldPersistAuditLogWithCorrectFields() {
        when(auditLogRepository.save(any(AuditLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        auditLogService.log(5, "AUDIT_REVIEW_CREATED", "AuditReview", 1);

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(captor.capture());

        AuditLog saved = captor.getValue();
        assertEquals(5, saved.getUserId());
        assertEquals("AUDIT_REVIEW_CREATED", saved.getAction());
        assertEquals("AuditReview", saved.getEntityType());
        assertEquals(1, saved.getRecordId());
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void log_shouldNotPropagate_whenRepositoryThrows() {
        when(auditLogRepository.save(any(AuditLog.class))).thenThrow(new RuntimeException("DB down"));

        assertDoesNotThrow(() -> auditLogService.log(5, "AUDIT_REVIEW_CREATED", "AuditReview", 1));

        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void search_shouldMapRepositoryResultsToResponses() {
        AuditLog entry = new AuditLog();
        entry.setAuditId(1L);
        entry.setUserId(5);
        entry.setAction("AUDIT_REVIEW_CREATED");
        entry.setEntityType("AuditReview");
        entry.setRecordId(1);
        entry.setTimestamp(LocalDateTime.of(2026, 7, 17, 10, 0));

        Pageable pageable = PageRequest.of(0, 10);
        Page<AuditLog> page = new PageImpl<>(List.of(entry), pageable, 1);

        when(auditLogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<AuditLogResponse> result = auditLogService.search(5, "AuditReview", "AUDIT_REVIEW_CREATED",
                LocalDateTime.of(2026, 1, 1, 0, 0), LocalDateTime.of(2026, 12, 31, 23, 59), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("AUDIT_REVIEW_CREATED", result.getContent().get(0).getAction());
        assertEquals(5, result.getContent().get(0).getUserId());
    }

    @Test
    void search_shouldApplyDefaultSort_whenPageableUnsorted() {
        Pageable unsortedPageable = PageRequest.of(0, 10);
        Page<AuditLog> emptyPage = new PageImpl<>(List.of(), unsortedPageable, 0);

        when(auditLogRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<AuditLogResponse> result = auditLogService.search(null, null, null, null, null, unsortedPageable);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(auditLogRepository).findAll(any(Specification.class), pageableCaptor.capture());

        assertEquals(0, result.getTotalElements());
        assertFalse(pageableCaptor.getValue().getSort().isUnsorted());
    }
}
