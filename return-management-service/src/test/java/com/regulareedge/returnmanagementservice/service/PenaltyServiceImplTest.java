package com.regulareedge.returnmanagementservice.service;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceType;
import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import com.regulareedge.returnmanagementservice.dto.request.PenaltyProceedingRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdatePenaltyStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.PenaltyProceedingResponse;
import com.regulareedge.returnmanagementservice.entity.PenaltyProceeding;
import com.regulareedge.returnmanagementservice.entity.RegulatoryCorrespondence;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.repository.PenaltyProceedingRepository;
import com.regulareedge.returnmanagementservice.repository.RegulatoryCorrespondenceRepository;
import com.regulareedge.returnmanagementservice.service.implementation.PenaltyServiceImpl;
import com.regulareedge.returnmanagementservice.service.interfaces.AuditLogService;
import com.regulareedge.returnmanagementservice.service.interfaces.ComplianceReferenceValidationService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PenaltyServiceImplTest {

    @Mock
    private PenaltyProceedingRepository penaltyProceedingRepository;

    @Mock
    private RegulatoryCorrespondenceRepository correspondenceRepository;

    @Mock
    private ComplianceReferenceValidationService complianceReferenceValidationService;

    @Mock
    private AuditLogService auditLogService;

    private PenaltyServiceImpl penaltyService;

    private RegulatoryCorrespondence correspondence;
    private PenaltyProceeding penalty;

    @BeforeEach
    void setUp() {
        penaltyService = new PenaltyServiceImpl(
                penaltyProceedingRepository, correspondenceRepository, complianceReferenceValidationService,
                auditLogService);

        correspondence = new RegulatoryCorrespondence();
        correspondence.setCorrespondenceId(1);
        correspondence.setRegulatorId(3);
        correspondence.setType(CorrespondenceType.SHOW_CAUSE);
        correspondence.setStatus(CorrespondenceStatus.OPEN);

        penalty = new PenaltyProceeding();
        penalty.setPenaltyId(100);
        penalty.setCorrespondence(correspondence);
        penalty.setObligationId(10);
        penalty.setPenaltyType("Late Filing");
        penalty.setPenaltyAmount(5000.0);
        penalty.setStatus(PenaltyStatus.OPEN);
    }

    @Test
    void add_shouldPersistPenalty_whenReferencesValid() {
        PenaltyProceedingRequest request = new PenaltyProceedingRequest();
        request.setCorrespondenceId(1);
        request.setObligationId(10);
        request.setPenaltyType("Late Filing");
        request.setPenaltyAmount(5000.0);

        when(correspondenceRepository.findById(1)).thenReturn(Optional.of(correspondence));
        when(complianceReferenceValidationService.obligationExists(10)).thenReturn(true);
        when(penaltyProceedingRepository.save(any(PenaltyProceeding.class))).thenReturn(penalty);

        PenaltyProceedingResponse response = penaltyService.add(request);

        assertEquals("Late Filing", response.getPenaltyType());
        assertEquals(PenaltyStatus.OPEN, response.getStatus());
        org.mockito.Mockito.verify(auditLogService).log(null, "PENALTY_CREATED", "PenaltyProceeding", 100);
    }

    @Test
    void add_shouldThrow_whenCorrespondenceMissing() {
        PenaltyProceedingRequest request = new PenaltyProceedingRequest();
        request.setCorrespondenceId(404);
        request.setObligationId(10);
        request.setPenaltyType("Late Filing");
        request.setPenaltyAmount(5000.0);

        when(correspondenceRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> penaltyService.add(request));
    }

    @Test
    void add_shouldThrow_whenObligationNotFound() {
        PenaltyProceedingRequest request = new PenaltyProceedingRequest();
        request.setCorrespondenceId(1);
        request.setObligationId(999);
        request.setPenaltyType("Late Filing");
        request.setPenaltyAmount(5000.0);

        when(correspondenceRepository.findById(1)).thenReturn(Optional.of(correspondence));
        when(complianceReferenceValidationService.obligationExists(999)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> penaltyService.add(request));
    }

    @Test
    void getByStatus_shouldReturnMatchingPenalties() {
        when(penaltyProceedingRepository.findByStatus(PenaltyStatus.OPEN)).thenReturn(List.of(penalty));

        List<PenaltyProceedingResponse> responses = penaltyService.getByStatus("OPEN");

        assertEquals(1, responses.size());
        assertEquals(PenaltyStatus.OPEN, responses.get(0).getStatus());
    }

    @Test
    void updateStatus_shouldSetPaidDate_whenStatusPaid() {
        when(penaltyProceedingRepository.findById(100)).thenReturn(Optional.of(penalty));
        when(penaltyProceedingRepository.save(any(PenaltyProceeding.class))).thenReturn(penalty);

        UpdatePenaltyStatusRequest request = new UpdatePenaltyStatusRequest();
        request.setPenaltyId(100);
        request.setStatus(PenaltyStatus.PAID);
        request.setPaidDate(LocalDate.of(2026, 2, 1));

        PenaltyProceedingResponse response = penaltyService.updateStatus(request);

        assertEquals(PenaltyStatus.PAID, response.getStatus());
        assertEquals(LocalDate.of(2026, 2, 1), penalty.getPaidDate());
    }

    @Test
    void updateStatus_shouldThrow_whenPenaltyMissing() {
        UpdatePenaltyStatusRequest request = new UpdatePenaltyStatusRequest();
        request.setPenaltyId(404);
        request.setStatus(PenaltyStatus.PAID);

        when(penaltyProceedingRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> penaltyService.updateStatus(request));
    }
}
