package com.regulareedge.compliancecoreservice.service.interfaces;

import com.regulareedge.compliancecoreservice.dto.request.DataCollectionCreateRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateDataRequestStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCollectionRequestResponse;

import java.util.List;

public interface DataCollectionService {

    DataCollectionRequestResponse create(DataCollectionCreateRequest request);

    List<DataCollectionRequestResponse> getByDataOwner(int dataOwnerId);

    DataCollectionRequestResponse updateStatus(UpdateDataRequestStatusRequest request);

    List<DataCollectionRequestResponse> getAll();
}
