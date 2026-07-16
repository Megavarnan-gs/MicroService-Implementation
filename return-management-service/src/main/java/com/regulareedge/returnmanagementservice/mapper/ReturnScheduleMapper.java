package com.regulareedge.returnmanagementservice.mapper;

import com.regulareedge.returnmanagementservice.dto.response.ReturnScheduleResponse;
import com.regulareedge.returnmanagementservice.entity.ReturnSchedule;

public final class ReturnScheduleMapper {

    private ReturnScheduleMapper() {
    }

    public static ReturnScheduleResponse toResponse(ReturnSchedule entity) {
        if (entity == null) {
            return null;
        }
        return new ReturnScheduleResponse(
                entity.getScheduleId(),
                entity.getReturnId(),
                entity.getScheduleName(),
                entity.getDataContent(),
                entity.getValidatedById(),
                entity.getStatus());
    }
}
