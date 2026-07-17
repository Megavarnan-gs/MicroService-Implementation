package com.regulareedge.auditanalyticsservice.service;

import com.regulareedge.auditanalyticsservice.common.enums.EvidenceStatus;
import com.regulareedge.auditanalyticsservice.dto.request.ControlEvidenceRequest;
import com.regulareedge.auditanalyticsservice.dto.request.UpdateEvidenceStatusRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ControlEvidenceResponse;
import com.regulareedge.auditanalyticsservice.entity.ControlEvidence;
import com.regulareedge.auditanalyticsservice.exception.ResourceNotFoundException;
import com.regulareedge.auditanalyticsservice.repository.ControlEvidenceRepository;
import com.regulareedge.auditanalyticsservice.service.implementation.ControlEvidenceServiceImpl;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ControlEvidenceServiceImplTest {

    @Mock
    private ControlEvidenceRepository controlEvidenceRepository;

    @Mock
    private AuditLogService auditLogService;

    private ControlEvidenceServiceImpl controlEvidenceService;

    private ControlEvidence evidence;

    @BeforeEach
    void setUp() {
        controlEvidenceService = new ControlEvidenceServiceImpl(controlEvidenceRepository, auditLogService);

        evidence = new ControlEvidence();
        evidence.setEvidenceId(1);
        evidence.setReturnId(100);
        evidence.setEvidenceType("Screenshot");
        evidence.setFilePath("/files/evidence1.png");
        evidence.setUploadedById(7);
        evidence.setUploadedDate(LocalDate.of(2026, 4, 1));
        evidence.setStatus(EvidenceStatus.UPLOADED);
    }

    @Test
    void upload_shouldPersistEvidenceAsUploaded() {
        ControlEvidenceRequest request = new ControlEvidenceRequest();
        request.setReturnId(100);
        request.setEvidenceType("Screenshot");
        request.setFilePath("/files/evidence1.png");
        request.setUploadedById(7);
        request.setUploadedDate(LocalDate.of(2026, 4, 1));

        when(controlEvidenceRepository.save(any(ControlEvidence.class))).thenReturn(evidence);

        ControlEvidenceResponse response = controlEvidenceService.upload(request);

        assertEquals(EvidenceStatus.UPLOADED, response.getStatus());
        assertEquals(100, response.getReturnId());

        verify(auditLogService).log(7, "EVIDENCE_UPLOADED", "ControlEvidence", 1);
    }

    @Test
    void getByReturn_shouldReturnMatchingEvidence() {
        when(controlEvidenceRepository.findByReturnId(100)).thenReturn(List.of(evidence));

        List<ControlEvidenceResponse> responses = controlEvidenceService.getByReturn(100);

        assertEquals(1, responses.size());
        assertEquals(100, responses.get(0).getReturnId());
    }

    @Test
    void updateStatus_shouldUpdateStatus() {
        when(controlEvidenceRepository.findById(1)).thenReturn(Optional.of(evidence));
        when(controlEvidenceRepository.save(any(ControlEvidence.class))).thenReturn(evidence);

        UpdateEvidenceStatusRequest request = new UpdateEvidenceStatusRequest();
        request.setEvidenceId(1);
        request.setStatus(EvidenceStatus.VERIFIED);

        ControlEvidenceResponse response = controlEvidenceService.updateStatus(request);

        assertEquals(EvidenceStatus.VERIFIED, response.getStatus());
    }

    @Test
    void updateStatus_shouldThrow_whenEvidenceMissing() {
        when(controlEvidenceRepository.findById(404)).thenReturn(Optional.empty());

        UpdateEvidenceStatusRequest request = new UpdateEvidenceStatusRequest();
        request.setEvidenceId(404);
        request.setStatus(EvidenceStatus.VERIFIED);

        assertThrows(ResourceNotFoundException.class, () -> controlEvidenceService.updateStatus(request));
    }
}
