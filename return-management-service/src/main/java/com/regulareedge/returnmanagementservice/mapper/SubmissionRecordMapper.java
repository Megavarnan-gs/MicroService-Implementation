package com.regulareedge.returnmanagementservice.mapper;

import com.regulareedge.returnmanagementservice.dto.response.SubmissionRecordResponse;
import com.regulareedge.returnmanagementservice.entity.SubmissionRecord;

public final class SubmissionRecordMapper {

    private SubmissionRecordMapper() {
    }

    public static SubmissionRecordResponse toResponse(SubmissionRecord entity) {
        if (entity == null) {
            return null;
        }
        return new SubmissionRecordResponse(
                entity.getSubmissionId(),
                entity.getReturnId(),
                entity.getSubmittedById(),
                entity.getSubmissionDateTime(),
                entity.getSubmissionMode(),
                entity.getAcknowledgementRef(),
                entity.getStatus());
    }
}
