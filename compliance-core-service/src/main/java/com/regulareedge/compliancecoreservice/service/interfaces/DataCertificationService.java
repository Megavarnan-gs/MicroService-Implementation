package com.regulareedge.compliancecoreservice.service.interfaces;

import com.regulareedge.compliancecoreservice.dto.request.DataCertificationRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCertificationResponse;

import java.util.List;

public interface DataCertificationService {

    DataCertificationResponse create(DataCertificationRequest request);

    List<DataCertificationResponse> getByRequest(int requestId);
}
