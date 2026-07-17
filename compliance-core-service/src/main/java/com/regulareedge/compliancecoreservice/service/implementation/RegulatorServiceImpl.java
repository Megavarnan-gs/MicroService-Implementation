package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import com.regulareedge.compliancecoreservice.dto.request.RegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatorResponse;
import com.regulareedge.compliancecoreservice.entity.Regulator;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.mapper.RegulatorMapper;
import com.regulareedge.compliancecoreservice.repository.RegulatorRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.AuditLogService;
import com.regulareedge.compliancecoreservice.service.interfaces.RegulatorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegulatorServiceImpl implements RegulatorService {

    private final RegulatorRepository regulatorRepository;
    private final AuditLogService auditLogService;

    public RegulatorServiceImpl(RegulatorRepository regulatorRepository, AuditLogService auditLogService) {
        this.regulatorRepository = regulatorRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public RegulatorResponse create(RegulatorRequest request) {
        Regulator regulator = new Regulator();
        regulator.setName(request.getName());
        regulator.setJurisdiction(request.getJurisdiction());
        regulator.setRegulatoryDomain(request.getRegulatoryDomain());
        regulator.setContactDetails(request.getContactDetails());
        regulator.setStatus(RegulatorStatus.ACTIVE);

        Regulator saved = regulatorRepository.save(regulator);
        auditLogService.log(null, "REGULATOR_CREATED", "Regulator", saved.getRegulatorId());
        return RegulatorMapper.toResponse(saved);
    }

    @Override
    public List<RegulatorResponse> getAll() {
        return regulatorRepository.findAll().stream()
                .map(RegulatorMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatorResponse> getActive() {
        return regulatorRepository.findByStatus(RegulatorStatus.ACTIVE).stream()
                .map(RegulatorMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(int regulatorId) {
        Regulator regulator = regulatorRepository.findById(regulatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Regulator not found with id: " + regulatorId));

        regulatorRepository.delete(regulator);
        auditLogService.log(null, "REGULATOR_DELETED", "Regulator", regulatorId);
    }
}
