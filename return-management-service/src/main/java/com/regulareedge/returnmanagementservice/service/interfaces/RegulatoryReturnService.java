package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.dto.request.RegulatoryReturnRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateReturnStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryReturnResponse;

import java.util.List;

public interface RegulatoryReturnService {

    RegulatoryReturnResponse create(RegulatoryReturnRequest request);

    List<RegulatoryReturnResponse> getMyReturns(int preparedById);

    List<RegulatoryReturnResponse> getByStatus(String status);

    List<RegulatoryReturnResponse> getAll();

    List<RegulatoryReturnResponse> getPendingApprovals();

    RegulatoryReturnResponse updateStatus(UpdateReturnStatusRequest request);
}
