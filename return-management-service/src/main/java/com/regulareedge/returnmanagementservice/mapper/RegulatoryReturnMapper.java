package com.regulareedge.returnmanagementservice.mapper;

import com.regulareedge.returnmanagementservice.dto.response.RegulatoryReturnResponse;
import com.regulareedge.returnmanagementservice.entity.RegulatoryReturn;

public final class RegulatoryReturnMapper {

    private RegulatoryReturnMapper() {
    }

    public static RegulatoryReturnResponse toResponse(RegulatoryReturn entity) {
        if (entity == null) {
            return null;
        }
        return new RegulatoryReturnResponse(
                entity.getReturnId(),
                entity.getObligationId(),
                entity.getCalendarId(),
                entity.getReportingPeriod(),
                entity.getPreparedById(),
                entity.getTotalSchedules(),
                entity.getSubmissionDeadline(),
                entity.getStatus());
    }
}
