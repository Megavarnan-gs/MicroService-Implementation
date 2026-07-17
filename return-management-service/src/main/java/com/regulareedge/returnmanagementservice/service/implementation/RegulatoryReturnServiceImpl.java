package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.common.enums.ReturnStatus;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryReturnRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateReturnStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryReturnResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryReturn;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.InvalidStatusTransitionException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.mapper.RegulatoryReturnMapper;
import com.regulareedge.returnmanagementservice.repository.RegulatoryReturnRepository;
import com.regulareedge.returnmanagementservice.service.interfaces.AuditLogService;
import com.regulareedge.returnmanagementservice.service.interfaces.ComplianceReferenceValidationService;
import com.regulareedge.returnmanagementservice.service.interfaces.RegulatoryReturnService;
import com.regulareedge.returnmanagementservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegulatoryReturnServiceImpl implements RegulatoryReturnService {

    private static final Logger logger = LoggerFactory.getLogger(RegulatoryReturnServiceImpl.class);

    private final RegulatoryReturnRepository regulatoryReturnRepository;
    private final ComplianceReferenceValidationService complianceReferenceValidationService;
    private final UserValidationService userValidationService;
    private final AuditLogService auditLogService;

    public RegulatoryReturnServiceImpl(RegulatoryReturnRepository regulatoryReturnRepository,
                                        ComplianceReferenceValidationService complianceReferenceValidationService,
                                        UserValidationService userValidationService,
                                        AuditLogService auditLogService) {
        this.regulatoryReturnRepository = regulatoryReturnRepository;
        this.complianceReferenceValidationService = complianceReferenceValidationService;
        this.userValidationService = userValidationService;
        this.auditLogService = auditLogService;
    }

    @Override
    public RegulatoryReturnResponse create(RegulatoryReturnRequest request) {
        if (!complianceReferenceValidationService.obligationExists(request.getObligationId())) {
            throw new InvalidRequestException("Obligation not found with id: " + request.getObligationId());
        }
        if (!complianceReferenceValidationService.calendarExists(request.getObligationId(), request.getCalendarId())) {
            throw new InvalidRequestException("Calendar entry not found with id: " + request.getCalendarId());
        }
        if (!userValidationService.userExists(request.getPreparedById())) {
            logger.warn("preparedById={} was reported as not found by auth-service", request.getPreparedById());
            throw new InvalidRequestException("Preparer user not found with id: " + request.getPreparedById());
        }

        RegulatoryReturn regulatoryReturn = new RegulatoryReturn();
        regulatoryReturn.setObligationId(request.getObligationId());
        regulatoryReturn.setCalendarId(request.getCalendarId());
        regulatoryReturn.setReportingPeriod(request.getReportingPeriod());
        regulatoryReturn.setPreparedById(request.getPreparedById());
        regulatoryReturn.setTotalSchedules(request.getTotalSchedules());
        regulatoryReturn.setSubmissionDeadline(request.getSubmissionDeadline());
        regulatoryReturn.setStatus(ReturnStatus.DRAFT.name());

        RegulatoryReturn saved = regulatoryReturnRepository.save(regulatoryReturn);
        auditLogService.log(saved.getPreparedById(), "RETURN_CREATED", "RegulatoryReturn", saved.getReturnId());
        return RegulatoryReturnMapper.toResponse(saved);
    }

    @Override
    public List<RegulatoryReturnResponse> getMyReturns(int preparedById) {
        return regulatoryReturnRepository.findByPreparedById(preparedById).stream()
                .map(RegulatoryReturnMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryReturnResponse> getByStatus(String status) {
        validateStatus(status);
        return regulatoryReturnRepository.findByStatus(status).stream()
                .map(RegulatoryReturnMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryReturnResponse> getAll() {
        return regulatoryReturnRepository.findAll().stream()
                .map(RegulatoryReturnMapper::toResponse)
                .toList();
    }

    @Override
    public List<RegulatoryReturnResponse> getPendingApprovals() {
        return regulatoryReturnRepository.findByStatus(ReturnStatus.PENDING_CCO_APPROVAL.name()).stream()
                .map(RegulatoryReturnMapper::toResponse)
                .toList();
    }

    @Override
    public RegulatoryReturnResponse updateStatus(UpdateReturnStatusRequest request) {
        validateStatus(request.getStatus());

        RegulatoryReturn regulatoryReturn = regulatoryReturnRepository.findById(request.getReturnId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Regulatory return not found with id: " + request.getReturnId()));

        regulatoryReturn.setStatus(request.getStatus());
        RegulatoryReturn saved = regulatoryReturnRepository.save(regulatoryReturn);
        auditLogService.log(null, "RETURN_STATUS_UPDATED", "RegulatoryReturn", saved.getReturnId());
        return RegulatoryReturnMapper.toResponse(saved);
    }

    private void validateStatus(String status) {
        try {
            ReturnStatus.valueOf(status);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new InvalidStatusTransitionException("Invalid return status: " + status);
        }
    }
}
