package com.regulareedge.compliancecoreservice.service.interfaces;

import com.regulareedge.compliancecoreservice.dto.request.DataQualityCheckRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataQualityCheckResponse;

import java.util.List;

public interface DataQualityService {

    DataQualityCheckResponse create(DataQualityCheckRequest request);

    List<DataQualityCheckResponse> getByRequest(int requestId);

    List<DataQualityCheckResponse> getAll();
}
