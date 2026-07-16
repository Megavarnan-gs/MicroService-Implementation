package com.regulareedge.returnmanagementservice.mapper;

import com.regulareedge.returnmanagementservice.dto.response.PenaltyProceedingResponse;
import com.regulareedge.returnmanagementservice.entity.PenaltyProceeding;

public final class PenaltyMapper {

    private PenaltyMapper() {
    }

    public static PenaltyProceedingResponse toResponse(PenaltyProceeding entity) {
        if (entity == null) {
            return null;
        }
        return new PenaltyProceedingResponse(
                entity.getPenaltyId(),
                entity.getCorrespondenceId(),
                entity.getObligationId(),
                entity.getPenaltyType(),
                entity.getPenaltyAmount(),
                entity.getDisputeStatus(),
                entity.getPaidDate(),
                entity.getStatus());
    }
}
