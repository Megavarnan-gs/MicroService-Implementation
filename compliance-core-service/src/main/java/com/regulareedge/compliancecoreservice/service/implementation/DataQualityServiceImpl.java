package com.regulareedge.compliancecoreservice.service.implementation;

import com.regulareedge.compliancecoreservice.dto.request.DataQualityCheckRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataQualityCheckResponse;
import com.regulareedge.compliancecoreservice.entity.DataCollectionRequest;
import com.regulareedge.compliancecoreservice.entity.DataQualityCheck;
import com.regulareedge.compliancecoreservice.exception.ResourceNotFoundException;
import com.regulareedge.compliancecoreservice.mapper.DataQualityCheckMapper;
import com.regulareedge.compliancecoreservice.repository.DataCollectionRequestRepository;
import com.regulareedge.compliancecoreservice.repository.DataQualityCheckRepository;
import com.regulareedge.compliancecoreservice.service.interfaces.DataQualityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataQualityServiceImpl implements DataQualityService {

    private final DataQualityCheckRepository checkRepository;
    private final DataCollectionRequestRepository requestRepository;

    public DataQualityServiceImpl(DataQualityCheckRepository checkRepository,
                                   DataCollectionRequestRepository requestRepository) {
        this.checkRepository = checkRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public DataQualityCheckResponse create(DataQualityCheckRequest request) {
        DataCollectionRequest dataRequest = requestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Data collection request not found with id: " + request.getRequestId()));

        DataQualityCheck check = new DataQualityCheck();
        check.setRequest(dataRequest);
        check.setRuleName(request.getRuleName());
        check.setRuleDescription(request.getRuleDescription());
        check.setExpectedValue(request.getExpectedValue());
        check.setActualValue(request.getActualValue());
        check.setOutcome(request.getOutcome());
        check.setCheckedDate(request.getCheckedDate());

        DataQualityCheck saved = checkRepository.save(check);
        return DataQualityCheckMapper.toResponse(saved);
    }

    @Override
    public List<DataQualityCheckResponse> getByRequest(int requestId) {
        return checkRepository.findByRequest_RequestId(requestId).stream()
                .map(DataQualityCheckMapper::toResponse)
                .toList();
    }

    @Override
    public List<DataQualityCheckResponse> getAll() {
        List<DataQualityCheckResponse> responses = new java.util.ArrayList<>();
        checkRepository.findAll().forEach(check -> responses.add(DataQualityCheckMapper.toResponse(check)));
        return responses;
    }
}
