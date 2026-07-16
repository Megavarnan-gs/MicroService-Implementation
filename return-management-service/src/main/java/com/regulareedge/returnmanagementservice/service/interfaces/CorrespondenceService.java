package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.dto.request.AssignCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateCorrespondenceStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryCorrespondenceResponse;

import java.util.List;

public interface CorrespondenceService {

    RegulatoryCorrespondenceResponse create(RegulatoryCorrespondenceRequest request);

    List<RegulatoryCorrespondenceResponse> getByRegulator(int regulatorId);

    List<RegulatoryCorrespondenceResponse> getAll();

    RegulatoryCorrespondenceResponse assign(AssignCorrespondenceRequest request);

    RegulatoryCorrespondenceResponse updateStatus(UpdateCorrespondenceStatusRequest request);
}
