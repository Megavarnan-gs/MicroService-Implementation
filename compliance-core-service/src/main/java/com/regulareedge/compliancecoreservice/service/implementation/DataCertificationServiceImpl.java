package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.common.enums.CertificationStatus;
import com.regulareedge.compliancecoreservice.dto.request.DataCertificationRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCertificationResponse;
import com.regulareedge.compliancecoreservice.entity.DataCertification;
import com.regulareedge.compliancecoreservice.entity.DataCollectionRequest;
import com.regulareedge.compliancecoreservice.exception.InvalidRequestException;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.mapper.DataCertificationMapper;
import com.regulareedge.compliancecoreservice.repository.DataCertificationRepository;
import com.regulareedge.compliancecoreservice.repository.DataCollectionRequestRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.DataCertificationService;
import com.regulareedge.compliancecoreservice.service.interfaces.UserValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCertificationServiceImpl implements DataCertificationService {

    private static final Logger logger = LoggerFactory.getLogger(DataCertificationServiceImpl.class);

    private final DataCertificationRepository certificationRepository;
    private final DataCollectionRequestRepository requestRepository;
    private final UserValidationService userValidationService;

    public DataCertificationServiceImpl(DataCertificationRepository certificationRepository,
                                         DataCollectionRequestRepository requestRepository,
                                         UserValidationService userValidationService) {
        this.certificationRepository = certificationRepository;
        this.requestRepository = requestRepository;
        this.userValidationService = userValidationService;
    }

    @Override
    public DataCertificationResponse create(DataCertificationRequest request) {
        DataCollectionRequest dataRequest = requestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data collection request not found with id: " + request.getRequestId()));

        // Fail-open certifier validation - see UserValidationServiceImpl / UserServiceClientFallback.
        if (!userValidationService.userExists(request.getCertifiedById())) {
            logger.warn("Certifier userId={} was reported as not found by auth-service",
                    request.getCertifiedById());
            throw new InvalidRequestException("Certifying user not found with id: " + request.getCertifiedById());
        }

        DataCertification certification = new DataCertification();
        certification.setRequest(dataRequest);
        certification.setCertifiedById(request.getCertifiedById());
        certification.setCertificationDate(request.getCertificationDate());
        certification.setCertificationStatement(request.getCertificationStatement());
        certification.setStatus(CertificationStatus.CERTIFIED);

        DataCertification saved = certificationRepository.save(certification);
        return DataCertificationMapper.toResponse(saved);
    }

    @Override
    public List<DataCertificationResponse> getByRequest(int requestId) {
        return certificationRepository.findByRequest_RequestId(requestId).stream()
                .map(DataCertificationMapper::toResponse)
                .toList();
    }

}
