package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.common.enums.CorrespondenceStatus;
import com.regulareedge.returnmanagementservice.dto.request.AssignCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateCorrespondenceStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryCorrespondenceResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryCorrespondence;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.mapper.CorrespondenceMapper;
import com.regulareedge.returnmanagementservice.repository.RegulatoryCorrespondenceRepository;
import com.regulareedge.returnmanagementservice.service.interfaces.ComplianceReferenceValidationService;
import com.regulareedge.returnmanagementservice.service.interfaces.CorrespondenceService;
import com.regulareedge.returnmanagementservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorrespondenceServiceImpl implements CorrespondenceService {

    private static final Logger logger = LoggerFactory.getLogger(CorrespondenceServiceImpl.class);

    private final RegulatoryCorrespondenceRepository correspondenceRepository;
    private final ComplianceReferenceValidationService complianceReferenceValidationService;
    private final UserValidationService userValidationService;

    public CorrespondenceServiceImpl(RegulatoryCorrespondenceRepository correspondenceRepository,
                                      ComplianceReferenceValidationService complianceReferenceValidationService,
                                      UserValidationService userValidationService) {
        this.correspondenceRepository = correspondenceRepository;
        this.complianceReferenceValidationService = complianceReferenceValidationService;
        this.userValidationService = userValidationService;
    }

    @Override
    public RegulatoryCorrespondenceResponse create(RegulatoryCorrespondenceRequest request) {
        if (!complianceReferenceValidationService.regulatorExists(request.getRegulatorId())) {
            throw new InvalidRequestException("Regulator not found with id: " + request.getRegulatorId());
        }
        if (!userValidationService.userExists(request.getAssignedToId())) {
            logger.warn("assignedToId={} was reported as not found by auth-service", request.getAssignedToId());
            throw new InvalidRequestException("Assignee user not found with id: " + request.getAssignedToId());
        }

        RegulatoryCorrespondence correspondence = new RegulatoryCorrespondence();
        correspondence.setRegulatorId(request.getRegulatorId());
        correspondence.setType(request.getType());
        correspondence.setReceivedDate(request.getReceivedDate());
        correspondence.setSubject(request.getSubject());
        correspondence.setResponseDueDate(request.getResponseDueDate());
        correspondence.setAssignedToId(request.getAssignedToId());
        correspondence.setStatus(CorrespondenceStatus.OPEN);

        RegulatoryCorrespondence saved = correspondenceRepository.save(correspondence);
        return CorrespondenceMapper.toResponse(saved);
    }

    @Override
    public List<RegulatoryCorrespondenceResponse> getByRegulator(int regulatorId) {
        return correspondenceRepository.findByRegulatorId(regulatorId).stream()
                .map(CorrespondenceMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryCorrespondenceResponse> getAll() {
        return correspondenceRepository.findAll().stream()
                .map(CorrespondenceMapper::toResponse)
                .toList();
    }

    @Override
    public RegulatoryCorrespondenceResponse assign(AssignCorrespondenceRequest request) {
        RegulatoryCorrespondence correspondence = correspondenceRepository.findById(request.getCorrespondenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Correspondence not found with id: " + request.getCorrespondenceId()));

        if (!userValidationService.userExists(request.getAssignedToId())) {
            logger.warn("assignedToId={} was reported as not found by auth-service", request.getAssignedToId());
            throw new InvalidRequestException("Assignee user not found with id: " + request.getAssignedToId());
        }

        correspondence.setAssignedToId(request.getAssignedToId());
        RegulatoryCorrespondence saved = correspondenceRepository.save(correspondence);
        return CorrespondenceMapper.toResponse(saved);
    }

    @Override
    public RegulatoryCorrespondenceResponse updateStatus(UpdateCorrespondenceStatusRequest request) {
        RegulatoryCorrespondence correspondence = correspondenceRepository.findById(request.getCorrespondenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Correspondence not found with id: " + request.getCorrespondenceId()));

        correspondence.setStatus(request.getStatus());
        RegulatoryCorrespondence saved = correspondenceRepository.save(correspondence);
        return CorrespondenceMapper.toResponse(saved);
    }
}
