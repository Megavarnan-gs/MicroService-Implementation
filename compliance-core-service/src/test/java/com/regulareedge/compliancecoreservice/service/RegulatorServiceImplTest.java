package com.regulareedge.compliancecoreservice.service;

import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import com.regulareedge.compliancecoreservice.dto.request.RegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatorResponse;
import com.regulareedge.compliancecoreservice.entity.Regulator;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.repository.RegulatorRepository;
import com.regulareedge.compliancecoreservice.service.implementation.RegulatorServiceImpl;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegulatorServiceImplTest {

    @Mock
    private RegulatorRepository regulatorRepository;

    private RegulatorServiceImpl regulatorService;

    private Regulator regulator;

    @BeforeEach
    void setUp() {
        regulatorService = new RegulatorServiceImpl(regulatorRepository);

        regulator = new Regulator();
        regulator.setRegulatorId(1);
        regulator.setName("Financial Conduct Authority");
        regulator.setJurisdiction("UK");
        regulator.setRegulatoryDomain("Banking");
        regulator.setContactDetails("contact@fca.example");
        regulator.setStatus(RegulatorStatus.ACTIVE);
    }

    @Test
    void create_shouldPersistActiveRegulator() {
        RegulatorRequest request = new RegulatorRequest();
        request.setName("Financial Conduct Authority");
        request.setJurisdiction("UK");
        request.setRegulatoryDomain("Banking");
        request.setContactDetails("contact@fca.example");

        when(regulatorRepository.save(any(Regulator.class))).thenReturn(regulator);

        RegulatorResponse response = regulatorService.create(request);

        assertEquals("Financial Conduct Authority", response.getName());
        assertEquals(RegulatorStatus.ACTIVE, response.getStatus());
    }

    @Test
    void getAll_shouldReturnAllRegulators() {
        when(regulatorRepository.findAll()).thenReturn(List.of(regulator));

        List<RegulatorResponse> responses = regulatorService.getAll();

        assertEquals(1, responses.size());
        assertEquals("Financial Conduct Authority", responses.get(0).getName());
    }

    @Test
    void getActive_shouldReturnOnlyActiveRegulators() {
        when(regulatorRepository.findByStatus(RegulatorStatus.ACTIVE)).thenReturn(List.of(regulator));

        List<RegulatorResponse> responses = regulatorService.getActive();

        assertEquals(1, responses.size());
        assertEquals(RegulatorStatus.ACTIVE, responses.get(0).getStatus());
    }

    @Test
    void delete_shouldRemoveRegulator_whenExists() {
        when(regulatorRepository.findById(1)).thenReturn(Optional.of(regulator));

        regulatorService.delete(1);

        verify(regulatorRepository).delete(regulator);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenMissing() {
        when(regulatorRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> regulatorService.delete(99));
    }
}
