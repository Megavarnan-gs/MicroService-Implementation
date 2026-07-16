package com.regulareedge.returnmanagementservice.service;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceType;
import com.regulareedge.returnmanagementservice.dto.request.AssignCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateCorrespondenceStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryCorrespondenceResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryCorrespondence;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.repository.RegulatoryCorrespondenceRepository;
import com.regulareedge.returnmanagementservice.service.implementation.CorrespondenceServiceImpl;
import com.regulareedge.returnmanagementservice.service.interfaces.ComplianceReferenceValidationService;
import com.regulareedge.returnmanagementservice.service.interfaces.UserValidationService;
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
class CorrespondenceServiceImplTest {

    @Mock
    private RegulatoryCorrespondenceRepository correspondenceRepository;

    @Mock
    private ComplianceReferenceValidationService complianceReferenceValidationService;

    @Mock
    private UserValidationService userValidationService;

    private CorrespondenceServiceImpl correspondenceService;

    private RegulatoryCorrespondence correspondence;

    @BeforeEach
    void setUp() {
        correspondenceService = new CorrespondenceServiceImpl(
                correspondenceRepository, complianceReferenceValidationService, userValidationService);

        correspondence = new RegulatoryCorrespondence();
        correspondence.setCorrespondenceId(1);
        correspondence.setRegulatorId(3);
        correspondence.setType(CorrespondenceType.NOTICE);
        correspondence.setReceivedDate(LocalDate.of(2026, 1, 10));
        correspondence.setSubject("Late filing notice");
        correspondence.setAssignedToId(7);
        correspondence.setStatus(CorrespondenceStatus.OPEN);
    }

    @Test
    void create_shouldPersistCorrespondence_whenReferencesValid() {
        RegulatoryCorrespondenceRequest request = new RegulatoryCorrespondenceRequest();
        request.setRegulatorId(3);
        request.setType(CorrespondenceType.NOTICE);
        request.setReceivedDate(LocalDate.of(2026, 1, 10));
        request.setSubject("Late filing notice");
        request.setAssignedToId(7);

        when(complianceReferenceValidationService.regulatorExists(3)).thenReturn(true);
        when(userValidationService.userExists(7)).thenReturn(true);
        when(correspondenceRepository.save(any(RegulatoryCorrespondence.class))).thenReturn(correspondence);

        RegulatoryCorrespondenceResponse response = correspondenceService.create(request);

        assertEquals("Late filing notice", response.getSubject());
        assertEquals(CorrespondenceStatus.OPEN, response.getStatus());
    }

    @Test
    void create_shouldThrow_whenRegulatorNotFound() {
        RegulatoryCorrespondenceRequest request = new RegulatoryCorrespondenceRequest();
        request.setRegulatorId(99);
        request.setType(CorrespondenceType.NOTICE);
        request.setReceivedDate(LocalDate.of(2026, 1, 10));
        request.setSubject("Late filing notice");
        request.setAssignedToId(7);

        when(complianceReferenceValidationService.regulatorExists(99)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> correspondenceService.create(request));
    }

    @Test
    void getByRegulator_shouldReturnMatchingCorrespondence() {
        when(correspondenceRepository.findByRegulatorId(3)).thenReturn(List.of(correspondence));

        List<RegulatoryCorrespondenceResponse> responses = correspondenceService.getByRegulator(3);

        assertEquals(1, responses.size());
        assertEquals(3, responses.get(0).getRegulatorId());
    }

    @Test
    void assign_shouldUpdateAssignee_whenExists() {
        when(correspondenceRepository.findById(1)).thenReturn(Optional.of(correspondence));
        when(userValidationService.userExists(9)).thenReturn(true);
        when(correspondenceRepository.save(any(RegulatoryCorrespondence.class))).thenReturn(correspondence);

        AssignCorrespondenceRequest request = new AssignCorrespondenceRequest();
        request.setCorrespondenceId(1);
        request.setAssignedToId(9);

        RegulatoryCorrespondenceResponse response = correspondenceService.assign(request);

        assertEquals(1, response.getCorrespondenceId());
    }

    @Test
    void updateStatus_shouldThrow_whenCorrespondenceMissing() {
        UpdateCorrespondenceStatusRequest request = new UpdateCorrespondenceStatusRequest();
        request.setCorrespondenceId(404);
        request.setStatus(CorrespondenceStatus.CLOSED);

        when(correspondenceRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> correspondenceService.updateStatus(request));
    }

    @Test
    void updateStatus_shouldUpdateStatus_whenExists() {
        when(correspondenceRepository.findById(1)).thenReturn(Optional.of(correspondence));
        when(correspondenceRepository.save(any(RegulatoryCorrespondence.class))).thenReturn(correspondence);

        UpdateCorrespondenceStatusRequest request = new UpdateCorrespondenceStatusRequest();
        request.setCorrespondenceId(1);
        request.setStatus(CorrespondenceStatus.CLOSED);

        RegulatoryCorrespondenceResponse response = correspondenceService.updateStatus(request);

        assertEquals(1, response.getCorrespondenceId());
    }
}
