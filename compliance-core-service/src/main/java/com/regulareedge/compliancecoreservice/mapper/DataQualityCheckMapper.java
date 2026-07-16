package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.DataQualityCheckResponse;
import com.regulareedge.compliancecoreservice.entity.DataQualityCheck;

public final class DataQualityCheckMapper {

    private DataQualityCheckMapper() {
    }

    public static DataQualityCheckResponse toResponse(DataQualityCheck check) {
        if (check == null) {
            return null;
        }
        return new DataQualityCheckResponse(
                check.getCheckId(),
                check.getRequestId(),
                check.getRuleName(),
                check.getRuleDescription(),
                check.getExpectedValue(),
                check.getActualValue(),
                check.getOutcome(),
                check.getCheckedDate());
    }
}
