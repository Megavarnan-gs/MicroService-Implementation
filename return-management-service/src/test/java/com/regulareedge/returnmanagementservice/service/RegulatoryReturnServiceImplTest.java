package com.regulareedge.returnmanagementservice.service;

import com.regulareedge.returnmanagementservice.common.enums.ReturnStatus;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryReturnRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateReturnStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryReturnResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryReturn;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.InvalidStatusTransitionException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.repository.RegulatoryReturnRepository;
import com.regulareedge.returnmanagementservice.service.implementation.RegulatoryReturnServiceImpl;
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
class RegulatoryReturnServiceImplTest {

    @Mock
    private RegulatoryReturnRepository regulatoryReturnRepository;

    @Mock
    private ComplianceReferenceValidationService complianceReferenceValidationService;

    @Mock
    private UserValidationService userValidationService;

    private RegulatoryReturnServiceImpl regulatoryReturnService;

    private RegulatoryReturn regulatoryReturn;

    @BeforeEach
    void setUp() {
        regulatoryReturnService = new RegulatoryReturnServiceImpl(
                regulatoryReturnRepository, complianceReferenceValidationService, userValidationService);

        regulatoryReturn = new RegulatoryReturn();
        regulatoryReturn.setReturnId(1);
        regulatoryReturn.setObligationId(10);
        regulatoryReturn.setCalendarId(20);
        regulatoryReturn.setReportingPeriod("Q1-2026");
        regulatoryReturn.setPreparedById(5);
        regulatoryReturn.setTotalSchedules(2);
        regulatoryReturn.setSubmissionDeadline(LocalDate.of(2026, 3, 31));
        regulatoryReturn.setStatus(ReturnStatus.DRAFT.name());
    }

    @Test
    void create_shouldPersistReturn_whenReferencesValid() {
        RegulatoryReturnRequest request = new RegulatoryReturnRequest();
        request.setObligationId(10);
        request.setCalendarId(20);
        request.setReportingPeriod("Q1-2026");
        request.setPreparedById(5);
        request.setTotalSchedules(2);
        request.setSubmissionDeadline(LocalDate.of(2026, 3, 31));

        when(complianceReferenceValidationService.obligationExists(10)).thenReturn(true);
        when(complianceReferenceValidationService.calendarExists(10, 20)).thenReturn(true);
        when(userValidationService.userExists(5)).thenReturn(true);
        when(regulatoryReturnRepository.save(any(RegulatoryReturn.class))).thenReturn(regulatoryReturn);

        RegulatoryReturnResponse response = regulatoryReturnService.create(request);

        assertEquals("Q1-2026", response.getReportingPeriod());
        assertEquals(ReturnStatus.DRAFT.name(), response.getStatus());
    }

    @Test
    void create_shouldThrow_whenObligationNotFound() {
        RegulatoryReturnRequest request = new RegulatoryReturnRequest();
        request.setObligationId(99);
        request.setCalendarId(20);
        request.setReportingPeriod("Q1-2026");
        request.setPreparedById(5);
        request.setSubmissionDeadline(LocalDate.of(2026, 3, 31));

        when(complianceReferenceValidationService.obligationExists(99)).thenReturn(false);

        assertThrows(InvalidRequestException.class, () -> regulatoryReturnService.create(request));
    }

    @Test
    void getPendingApprovals_shouldReturnMatchingReturns() {
        regulatoryReturn.setStatus(ReturnStatus.PENDING_CCO_APPROVAL.name());
        when(regulatoryReturnRepository.findByStatus(ReturnStatus.PENDING_CCO_APPROVAL.name()))
                .thenReturn(List.of(regulatoryReturn));

        List<RegulatoryReturnResponse> responses = regulatoryReturnService.getPendingApprovals();

        assertEquals(1, responses.size());
        assertEquals(ReturnStatus.PENDING_CCO_APPROVAL.name(), responses.get(0).getStatus());
    }

    @Test
    void updateStatus_shouldUpdateAndReturn_whenExists() {
        when(regulatoryReturnRepository.findById(1)).thenReturn(Optional.of(regulatoryReturn));
        when(regulatoryReturnRepository.save(any(RegulatoryReturn.class))).thenReturn(regulatoryReturn);

        UpdateReturnStatusRequest request = new UpdateReturnStatusRequest();
        request.setReturnId(1);
        request.setStatus(ReturnStatus.SUBMITTED.name());

        RegulatoryReturnResponse response = regulatoryReturnService.updateStatus(request);

        assertEquals(1, response.getReturnId());
    }

    @Test
    void updateStatus_shouldThrow_whenReturnMissing() {
        UpdateReturnStatusRequest request = new UpdateReturnStatusRequest();
        request.setReturnId(404);
        request.setStatus(ReturnStatus.SUBMITTED.name());

        when(regulatoryReturnRepository.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> regulatoryReturnService.updateStatus(request));
    }

    @Test
    void updateStatus_shouldThrow_whenStatusInvalid() {
        UpdateReturnStatusRequest request = new UpdateReturnStatusRequest();
        request.setReturnId(1);
        request.setStatus("NOT_A_REAL_STATUS");

        assertThrows(InvalidStatusTransitionException.class, () -> regulatoryReturnService.updateStatus(request));
    }
}
