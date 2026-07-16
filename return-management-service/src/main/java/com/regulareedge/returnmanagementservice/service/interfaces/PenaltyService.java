package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import com.regulareedge.returnmanagementservice.dto.request.PenaltyProceedingRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdatePenaltyStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.PenaltyProceedingResponse;
import com.regulareedge.returnmanagementservice.entity.PenaltyProceeding;

import java.util.List;

public interface PenaltyService {

    PenaltyProceedingResponse add(PenaltyProceedingRequest request);

    List<PenaltyProceedingResponse> getAll();

    List<PenaltyProceedingResponse> getByStatus(String status);

    PenaltyProceedingResponse updateStatus(UpdatePenaltyStatusRequest request);

    /** Used internally by DashboardService to avoid re-mapping to response DTOs. */
    List<PenaltyProceeding> getEntitiesByStatus(PenaltyStatus status);
}
