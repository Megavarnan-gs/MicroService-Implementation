package com.regulareedge.compliancecoreservice.service;

import com.regulareedge.compliancecoreservice.common.enums.Frequency;
import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;
import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import com.regulareedge.compliancecoreservice.dto.request.RegulatoryObligationRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateObligationStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatoryObligationResponse;
import com.regulareedge.compliancecoreservice.entity.Regulator;
import com.regulareedge.compliancecoreservice.entity.RegulatoryObligation;
import com.regulareedge.compliancecoreservice.exception.InvalidRequestException;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.repository.RegulatorRepository;
import com.regulareedge.compliancecoreservice.repository.RegulatoryObligationRepository;
import com.regulareedge.compliancecoreservice.service.implementation.ObligationServiceImpl;
import com.regulareedge.compliancecoreservice.service.interfaces.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObligationServiceImplTest {

    @Mock
    private RegulatoryObligationRepository obligationRepository;

    @Mock
    private RegulatorRepository regulatorRepository;

    @Mock
    private UserValidationService userValidationService;

    private ObligationServiceImpl obligationService;

    private Regulator activeRegulator;
    private RegulatoryObligation obligation;

    @BeforeEach
    void setUp() {
        obligationService = new ObligationServiceImpl(obligationRepository, regulatorRepository, userValidationService);

        activeRegulator = new Regulator();
        activeRegulator.setRegulatorId(1);
        activeRegulator.setName("FCA");
        activeRegulator.setStatus(RegulatorStatus.ACTIVE);

        obligation = new RegulatoryObligation();
        obligation.setObligationId(10);
        obligation.setRegulator(activeRegulator);
        obligation.setReturnName("Liquidity Return");
        obligation.setReturnCode("LR-01");
        obligation.setFrequency(Frequency.QUARTERLY);
        obligation.setOwnerId(5);
        obligation.setStatus(ObligationStatus.ACTIVE);
    }

    @Test
    void create_shouldPersistObligation_whenRegulatorActiveAndOwnerValid() {
        RegulatoryObligationRequest request = new RegulatoryObligationRequest();
        request.setRegulatorId(1);
        request.setReturnName("Liquidity Return");
        request.setReturnCode("LR-01");
        request.setFrequency(Frequency.QUARTERLY);
        request.setOwnerId(5);

        when(regulatorRepository.findById(1)).thenReturn(Optional.of(activeRegulator));
        when(userValidationService.userExists(5)).thenReturn(true);
        when(obligationRepository.save(any(RegulatoryObligation.class))).thenReturn(obligation);

        RegulatoryObligationResponse response = obligationService.create(request);

        assertEquals("Liquidity Return", response.getReturnName());
        assertEquals(ObligationStatus.ACTIVE, response.getStatus());
    }

    @Test
    void create_shouldThrow_whenRegulatorMissing() {
        RegulatoryObligationRequest request = new RegulatoryObligationRequest();
        request.setRegulatorId(99);
        request.setReturnName("Liquidity Return");
        request.setReturnCode("LR-01");
        request.setFrequency(Frequency.QUARTERLY);
        request.setOwnerId(5);

        when(regulatorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> obligationService.create(request));
    }

    @Test
    void create_shouldThrow_whenRegulatorNotActive() {
        Regulator inactiveRegulator = new Regulator();
        inactiveRegulator.setRegulatorId(2);
        inactiveRegulator.setStatus(RegulatorStatus.INACTIVE);

        RegulatoryObligationRequest request = new RegulatoryObligationRequest();
        request.setRegulatorId(2);
        request.setReturnName("Liquidity Return");
        request.setReturnCode("LR-01");
        request.setFrequency(Frequency.QUARTERLY);
        request.setOwnerId(5);

        when(regulatorRepository.findById(2)).thenReturn(Optional.of(inactiveRegulator));

        assertThrows(InvalidRequestException.class, () -> obligationService.create(request));
    }

    @Test
    void getByRegulator_shouldReturnMatchingObligations() {
        when(obligationRepository.findByRegulator_RegulatorId(1)).thenReturn(List.of(obligation));

        List<RegulatoryObligationResponse> responses = obligationService.getByRegulator(1);

        assertEquals(1, responses.size());
        assertEquals(1, responses.get(0).getRegulatorId());
    }

    @Test
    void updateStatus_shouldUpdateAndReturnObligation_whenExists() {
        when(obligationRepository.findById(10)).thenReturn(Optional.of(obligation));
        when(obligationRepository.save(any(RegulatoryObligation.class))).thenReturn(obligation);

        UpdateObligationStatusRequest request = new UpdateObligationStatusRequest();
        request.setObligationId(10);
        request.setStatus(ObligationStatus.ARCHIVED);

        RegulatoryObligationResponse response = obligationService.updateStatus(request);

        assertEquals(10, response.getObligationId());
    }

    @Test
    void updateStatus_shouldThrow_whenObligationMissing() {
        when(obligationRepository.findById(404)).thenReturn(Optional.empty());

        UpdateObligationStatusRequest request = new UpdateObligationStatusRequest();
        request.setObligationId(404);
        request.setStatus(ObligationStatus.ARCHIVED);

        assertThrows(ResourceNotFoundException.class, () -> obligationService.updateStatus(request));
    }
}
