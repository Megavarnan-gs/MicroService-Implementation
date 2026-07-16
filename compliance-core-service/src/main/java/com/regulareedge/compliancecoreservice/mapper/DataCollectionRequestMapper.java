package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.DataCollectionRequestResponse;
import com.regulareedge.compliancecoreservice.entity.DataCollectionRequest;

public final class DataCollectionRequestMapper {

    private DataCollectionRequestMapper() {
    }

    public static DataCollectionRequestResponse toResponse(DataCollectionRequest request) {
        if (request == null) {
            return null;
        }
        return new DataCollectionRequestResponse(
                request.getRequestId(),
                request.getCalendarId(),
                request.getDataOwnerId(),
                request.getDataDescription(),
                request.getDataCutOffDate(),
                request.getSubmissionDeadline(),
                request.getStatus());
    }
}
