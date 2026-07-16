package com.regulareedge.returnmanagementservice.mapper;

import com.regulareedge.returnmanagementservice.dto.response.RegulatoryCorrespondenceResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryCorrespondence;

public final class CorrespondenceMapper {

    private CorrespondenceMapper() {
    }

    public static RegulatoryCorrespondenceResponse toResponse(RegulatoryCorrespondence entity) {
        if (entity == null) {
            return null;
        }
        return new RegulatoryCorrespondenceResponse(
                entity.getCorrespondenceId(),
                entity.getRegulatorId(),
                entity.getType(),
                entity.getReceivedDate(),
                entity.getSubject(),
                entity.getResponseDueDate(),
                entity.getAssignedToId(),
                entity.getStatus());
    }
}
