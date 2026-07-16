package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.common.enums.SubmissionStatus;
import com.regulareedge.returnmanagementservice.dto.request.SubmissionRecordRequest;
import com.regulareedge.returnmanagementservice.dto.response.SubmissionRecordResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryReturn;
import com.regulareedge.returnmanagementservice.entity.SubmissionRecord;
import com.regulareedge.returnmanagementservice.exception.InvalidRequestException;
import com.regulareedge.returnmanagementservice.exception.ResourceNotFoundException;
import com.regulareedge.returnmanagementservice.mapper.SubmissionRecordMapper;
import com.regulareedge.returnmanagementservice.repository.RegulatoryReturnRepository;
import com.regulareedge.returnmanagementservice.repository.SubmissionRecordRepository;
import com.regulareedge.returnmanagementservice.service.interfaces.SubmissionRecordService;
import com.regulareedge.returnmanagementservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionRecordServiceImpl implements SubmissionRecordService {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionRecordServiceImpl.class);

    private final SubmissionRecordRepository submissionRecordRepository;
    private final RegulatoryReturnRepository regulatoryReturnRepository;
    private final UserValidationService userValidationService;

    public SubmissionRecordServiceImpl(SubmissionRecordRepository submissionRecordRepository,
                                        RegulatoryReturnRepository regulatoryReturnRepository,
                                        UserValidationService userValidationService) {
        this.submissionRecordRepository = submissionRecordRepository;
        this.regulatoryReturnRepository = regulatoryReturnRepository;
        this.userValidationService = userValidationService;
    }

    @Override
    public SubmissionRecordResponse record(SubmissionRecordRequest request) {
        RegulatoryReturn regulatoryReturn = regulatoryReturnRepository.findById(request.getReturnId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Regulatory return not found with id: " + request.getReturnId()));

        if (!userValidationService.userExists(request.getSubmittedById())) {
            logger.warn("submittedById={} was reported as not found by auth-service", request.getSubmittedById());
            throw new InvalidRequestException("User not found with id: " + request.getSubmittedById());
        }

        SubmissionRecord submissionRecord = new SubmissionRecord();
        submissionRecord.setRegulatoryReturn(regulatoryReturn);
        submissionRecord.setSubmittedById(request.getSubmittedById());
        submissionRecord.setSubmissionDateTime(LocalDateTime.now());
        submissionRecord.setSubmissionMode(request.getSubmissionMode());
        submissionRecord.setAcknowledgementRef(request.getAcknowledgementRef());
        submissionRecord.setStatus(request.getAcknowledgementRef() != null && !request.getAcknowledgementRef().isBlank()
                ? SubmissionStatus.SUCCESS
                : SubmissionStatus.PENDING);

        SubmissionRecord saved = submissionRecordRepository.save(submissionRecord);
        return SubmissionRecordMapper.toResponse(saved);
    }

    @Override
    public List<SubmissionRecordResponse> getByReturn(int returnId) {
        return submissionRecordRepository.findByRegulatoryReturn_ReturnId(returnId).stream()
                .map(SubmissionRecordMapper::toResponse)
                .toList();
    }
}
