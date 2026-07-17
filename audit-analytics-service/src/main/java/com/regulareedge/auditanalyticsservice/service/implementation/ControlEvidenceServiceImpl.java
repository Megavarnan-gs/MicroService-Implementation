package com.regulareedge.auditanalyticsservice.service.implementation;

import com.regulareedge.auditanalyticsservice.common.enums.EvidenceStatus;
import com.regulareedge.auditanalyticsservice.dto.request.ControlEvidenceRequest;
import com.regulareedge.auditanalyticsservice.dto.request.UpdateEvidenceStatusRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ControlEvidenceResponse;
import com.regulareedge.auditanalyticsservice.entity.ControlEvidence;
import com.regulareedge.auditanalyticsservice.exception.ResourceNotFoundException;
import com.regulareedge.auditanalyticsservice.mapper.ControlEvidenceMapper;
import com.regulareedge.auditanalyticsservice.repository.ControlEvidenceRepository;
import com.regulareedge.auditanalyticsservice.service.interfaces.AuditLogService;
import com.regulareedge.auditanalyticsservice.service.interfaces.ControlEvidenceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ControlEvidenceServiceImpl implements ControlEvidenceService {

    private final ControlEvidenceRepository controlEvidenceRepository;
    private final AuditLogService auditLogService;

    public ControlEvidenceServiceImpl(ControlEvidenceRepository controlEvidenceRepository,
                                       AuditLogService auditLogService) {
        this.controlEvidenceRepository = controlEvidenceRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public ControlEvidenceResponse upload(ControlEvidenceRequest request) {
        ControlEvidence evidence = new ControlEvidence();
        evidence.setReturnId(request.getReturnId());
        evidence.setEvidenceType(request.getEvidenceType());
        evidence.setFilePath(request.getFilePath());
        evidence.setUploadedById(request.getUploadedById());
        evidence.setUploadedDate(request.getUploadedDate());
        evidence.setStatus(EvidenceStatus.UPLOADED);

        ControlEvidence saved = controlEvidenceRepository.save(evidence);
        auditLogService.log(request.getUploadedById(), "EVIDENCE_UPLOADED", "ControlEvidence", saved.getEvidenceId());
        return ControlEvidenceMapper.toResponse(saved);
    }

    @Override
    public List<ControlEvidenceResponse> getByReturn(int returnId) {
        return controlEvidenceRepository.findByReturnId(returnId).stream()
                .map(ControlEvidenceMapper::toResponse)
                .toList();
    }

    @Override
    public ControlEvidenceResponse updateStatus(UpdateEvidenceStatusRequest request) {
        ControlEvidence evidence = controlEvidenceRepository.findById(request.getEvidenceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Control evidence not found with id: " + request.getEvidenceId()));

        evidence.setStatus(request.getStatus());

        ControlEvidence saved = controlEvidenceRepository.save(evidence);
        auditLogService.log(null, "EVIDENCE_STATUS_UPDATED", "ControlEvidence", saved.getEvidenceId());
        return ControlEvidenceMapper.toResponse(saved);
    }
}
