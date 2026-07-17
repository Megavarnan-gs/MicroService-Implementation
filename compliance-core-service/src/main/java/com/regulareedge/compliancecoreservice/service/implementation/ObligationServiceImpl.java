package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.common.enums.ObligationStatus;
import com.regulareedge.compliancecoreservice.common.enums.RegulatorStatus;
import com.regulareedge.compliancecoreservice.dto.request.RegulatoryObligationRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateObligationStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatoryObligationResponse;
import com.regulareedge.compliancecoreservice.entity.Regulator;
import com.regulareedge.compliancecoreservice.entity.RegulatoryObligation;
import com.regulareedge.compliancecoreservice.exception.InvalidRequestException;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.mapper.ObligationMapper;
import com.regulareedge.compliancecoreservice.repository.RegulatorRepository;
import com.regulareedge.compliancecoreservice.repository.RegulatoryObligationRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.AuditLogService;
import com.regulareedge.compliancecoreservice.service.interfaces.ObligationService;
import com.regulareedge.compliancecoreservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObligationServiceImpl implements ObligationService {

    private static final Logger logger = LoggerFactory.getLogger(ObligationServiceImpl.class);

    private final RegulatoryObligationRepository obligationRepository;
    private final RegulatorRepository regulatorRepository;
    private final UserValidationService userValidationService;
    private final AuditLogService auditLogService;

    public ObligationServiceImpl(RegulatoryObligationRepository obligationRepository,
                                  RegulatorRepository regulatorRepository,
                                  UserValidationService userValidationService,
                                  AuditLogService auditLogService) {
        this.obligationRepository = obligationRepository;
        this.regulatorRepository = regulatorRepository;
        this.userValidationService = userValidationService;
        this.auditLogService = auditLogService;
    }

    @Override
    public RegulatoryObligationResponse create(RegulatoryObligationRequest request) {
        Regulator regulator = regulatorRepository.findById(request.getRegulatorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Regulator not found with id: " + request.getRegulatorId()));

        if (regulator.getStatus() != RegulatorStatus.ACTIVE) {
            throw new InvalidRequestException(
                    "Regulator with id " + request.getRegulatorId() + " is not ACTIVE");
        }

        // Fail-open owner validation - see UserValidationServiceImpl / UserServiceClientFallback.
        if (!userValidationService.userExists(request.getOwnerId())) {
            logger.warn("Owner userId={} was reported as not found by auth-service", request.getOwnerId());
            throw new InvalidRequestException("Owner user not found with id: " + request.getOwnerId());
        }

        RegulatoryObligation obligation = new RegulatoryObligation();
        obligation.setRegulator(regulator);
        obligation.setReturnName(request.getReturnName());
        obligation.setReturnCode(request.getReturnCode());
        obligation.setFrequency(request.getFrequency());
        obligation.setSubmissionMode(request.getSubmissionMode());
        obligation.setReturnTemplateRef(request.getReturnTemplateRef());
        obligation.setOwnerId(request.getOwnerId());
        obligation.setStatus(ObligationStatus.ACTIVE);

        RegulatoryObligation saved = obligationRepository.save(obligation);
        auditLogService.log(request.getOwnerId(), "OBLIGATION_CREATED", "RegulatoryObligation", saved.getObligationId());
        return ObligationMapper.toResponse(saved);
    }

    @Override
    public List<RegulatoryObligationResponse> getAll() {
        return obligationRepository.findAll().stream()
                .map(ObligationMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryObligationResponse> getActive() {
        return obligationRepository.findByStatus(ObligationStatus.ACTIVE).stream()
                .map(ObligationMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryObligationResponse> getByRegulator(int regulatorId) {
        return obligationRepository.findByRegulator_RegulatorId(regulatorId).stream()
                .map(ObligationMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryObligationResponse> getByOwner(int ownerId) {
        return obligationRepository.findByOwnerId(ownerId).stream()
                .map(ObligationMapper::toResponse)
                .toList();
    }

    @Override
    public RegulatoryObligationResponse updateStatus(UpdateObligationStatusRequest request) {
        RegulatoryObligation obligation = obligationRepository.findById(request.getObligationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Obligation not found with id: " + request.getObligationId()));

        obligation.setStatus(request.getStatus());
        RegulatoryObligation saved = obligationRepository.save(obligation);
        auditLogService.log(saved.getOwnerId(), "OBLIGATION_STATUS_UPDATED", "RegulatoryObligation",
                saved.getObligationId());
        return ObligationMapper.toResponse(saved);
    }
}
