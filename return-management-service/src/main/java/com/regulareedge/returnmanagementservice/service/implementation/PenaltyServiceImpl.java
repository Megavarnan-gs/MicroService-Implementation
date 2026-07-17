package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import com.regulareedge.returnmanagementservice.dto.request.PenaltyProceedingRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdatePenaltyStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.PenaltyProceedingResponse;
import com.regulareedge.returnmanagementservice.entity.PenaltyProceeding;
import com.regulareedge.returnmanagementservice.entity.RegulatoryCorrespondence;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.InvalidStatusTransitionException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.mapper.PenaltyMapper;
import com.regulareedge.returnmanagementservice.repository.PenaltyProceedingRepository;
import com.regulareedge.returnmanagementservice.repository.RegulatoryCorrespondenceRepository;
import com.regulareedge.returnmanagementservice.service.interfaces.AuditLogService;
import com.regulareedge.returnmanagementservice.service.interfaces.ComplianceReferenceValidationService;
import com.regulareedge.returnmanagementservice.service.interfaces.PenaltyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PenaltyServiceImpl implements PenaltyService {

    private final PenaltyProceedingRepository penaltyProceedingRepository;
    private final RegulatoryCorrespondenceRepository correspondenceRepository;
    private final ComplianceReferenceValidationService complianceReferenceValidationService;
    private final AuditLogService auditLogService;

    public PenaltyServiceImpl(PenaltyProceedingRepository penaltyProceedingRepository,
                               RegulatoryCorrespondenceRepository correspondenceRepository,
                               ComplianceReferenceValidationService complianceReferenceValidationService,
                               AuditLogService auditLogService) {
        this.penaltyProceedingRepository = penaltyProceedingRepository;
        this.correspondenceRepository = correspondenceRepository;
        this.complianceReferenceValidationService = complianceReferenceValidationService;
        this.auditLogService = auditLogService;
    }

    @Override
    public PenaltyProceedingResponse add(PenaltyProceedingRequest request) {
        RegulatoryCorrespondence correspondence = correspondenceRepository.findById(request.getCorrespondenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Correspondence not found with id: " + request.getCorrespondenceId()));

        if (!complianceReferenceValidationService.obligationExists(request.getObligationId())) {
            throw new InvalidRequestException("Obligation not found with id: " + request.getObligationId());
        }

        PenaltyProceeding penalty = new PenaltyProceeding();
        penalty.setCorrespondence(correspondence);
        penalty.setObligationId(request.getObligationId());
        penalty.setPenaltyType(request.getPenaltyType());
        penalty.setPenaltyAmount(request.getPenaltyAmount());
        penalty.setDisputeStatus(request.getDisputeStatus());
        penalty.setStatus(PenaltyStatus.OPEN);

        PenaltyProceeding saved = penaltyProceedingRepository.save(penalty);
        auditLogService.log(null, "PENALTY_CREATED", "PenaltyProceeding", saved.getPenaltyId());
        return PenaltyMapper.toResponse(saved);
    }

    @Override
    public List<PenaltyProceedingResponse> getAll() {
        return penaltyProceedingRepository.findAll().stream()
                .map(PenaltyMapper::toResponse)
                .toList();
    }

    @Override
    public List<PenaltyProceedingResponse> getByStatus(String status) {
        PenaltyStatus penaltyStatus = parseStatus(status);
        return penaltyProceedingRepository.findByStatus(penaltyStatus).stream()
                .map(PenaltyMapper::toResponse)
                .toList();
    }

    @Override
    public PenaltyProceedingResponse updateStatus(UpdatePenaltyStatusRequest request) {
        PenaltyProceeding penalty = penaltyProceedingRepository.findById(request.getPenaltyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Penalty not found with id: " + request.getPenaltyId()));

        penalty.setStatus(request.getStatus());
        if (request.getStatus() == PenaltyStatus.PAID && request.getPaidDate() != null) {
            penalty.setPaidDate(request.getPaidDate());
        }

        PenaltyProceeding saved = penaltyProceedingRepository.save(penalty);
        auditLogService.log(null, "PENALTY_STATUS_UPDATED", "PenaltyProceeding", saved.getPenaltyId());
        return PenaltyMapper.toResponse(saved);
    }

    @Override
    public List<PenaltyProceeding> getEntitiesByStatus(PenaltyStatus status) {
        return penaltyProceedingRepository.findByStatus(status);
    }

    private PenaltyStatus parseStatus(String status) {
        try {
            return PenaltyStatus.valueOf(status);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new InvalidStatusTransitionException("Invalid penalty status: " + status);
        }
    }
}
