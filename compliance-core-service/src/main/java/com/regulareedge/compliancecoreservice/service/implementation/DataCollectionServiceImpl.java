package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.common.enums.DataRequestStatus;
import com.regulareedge.compliancecoreservice.dto.request.DataCollectionCreateRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateDataRequestStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCollectionRequestResponse;
import com.regulareedge.compliancecoreservice.entity.ComplianceCalendar;
import com.regulareedge.compliancecoreservice.entity.DataCollectionRequest;
import com.regulareedge.compliancecoreservice.exception.InvalidRequestException;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.mapper.DataCollectionRequestMapper;
import com.regulareedge.compliancecoreservice.repository.ComplianceCalendarRepository;
import com.regulareedge.compliancecoreservice.repository.DataCollectionRequestRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.AuditLogService;
import com.regulareedge.compliancecoreservice.service.interfaces.DataCollectionService;
import com.regulareedge.compliancecoreservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCollectionServiceImpl implements DataCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);

    private final DataCollectionRequestRepository requestRepository;
    private final ComplianceCalendarRepository calendarRepository;
    private final UserValidationService userValidationService;
    private final AuditLogService auditLogService;

    public DataCollectionServiceImpl(DataCollectionRequestRepository requestRepository,
                                      ComplianceCalendarRepository calendarRepository,
                                      UserValidationService userValidationService,
                                      AuditLogService auditLogService) {
        this.requestRepository = requestRepository;
        this.calendarRepository = calendarRepository;
        this.userValidationService = userValidationService;
        this.auditLogService = auditLogService;
    }

    @Override
    public DataCollectionRequestResponse create(DataCollectionCreateRequest request) {
        ComplianceCalendar calendar = calendarRepository.findById(request.getCalendarId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Calendar entry not found with id: " + request.getCalendarId()));

        // Fail-open owner validation - see UserValidationServiceImpl / UserServiceClientFallback.
        if (!userValidationService.userExists(request.getDataOwnerId())) {
            logger.warn("Data owner userId={} was reported as not found by auth-service",
                    request.getDataOwnerId());
            throw new InvalidRequestException("Data owner user not found with id: " + request.getDataOwnerId());
        }

        DataCollectionRequest entity = new DataCollectionRequest();
        entity.setCalendar(calendar);
        entity.setDataOwnerId(request.getDataOwnerId());
        entity.setDataDescription(request.getDataDescription());
        entity.setDataCutOffDate(request.getDataCutOffDate());
        entity.setSubmissionDeadline(request.getSubmissionDeadline());
        entity.setStatus(DataRequestStatus.PENDING);

        DataCollectionRequest saved = requestRepository.save(entity);
        auditLogService.log(request.getDataOwnerId(), "DATA_REQUEST_CREATED", "DataCollectionRequest",
                saved.getRequestId());
        return DataCollectionRequestMapper.toResponse(saved);
    }

    @Override
    public List<DataCollectionRequestResponse> getByDataOwner(int dataOwnerId) {
        return requestRepository.findByDataOwnerId(dataOwnerId).stream()
                .map(DataCollectionRequestMapper::toResponse)
                .toList();
    }

    @Override
    public DataCollectionRequestResponse updateStatus(UpdateDataRequestStatusRequest request) {
        DataCollectionRequest entity = requestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data collection request not found with id: " + request.getRequestId()));

        entity.setStatus(request.getStatus());
        DataCollectionRequest saved = requestRepository.save(entity);
        auditLogService.log(saved.getDataOwnerId(), "DATA_REQUEST_STATUS_UPDATED", "DataCollectionRequest",
                saved.getRequestId());
        return DataCollectionRequestMapper.toResponse(saved);
    }

    @Override
    public List<DataCollectionRequestResponse> getAll() {
        return requestRepository.findAll().stream()
                .map(DataCollectionRequestMapper::toResponse)
                .toList();
    }
}
