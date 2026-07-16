package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.RegulatoryObligationResponse;
import com.regulareedge.compliancecoreservice.entity.RegulatoryObligation;

public final class ObligationMapper {

    private ObligationMapper() {
    }

    public static RegulatoryObligationResponse toResponse(RegulatoryObligation obligation) {
        if (obligation == null) {
            return null;
        }
        return new RegulatoryObligationResponse(
                obligation.getObligationId(),
                obligation.getRegulatorId(),
                obligation.getReturnName(),
                obligation.getReturnCode(),
                obligation.getFrequency(),
                obligation.getSubmissionMode(),
                obligation.getReturnTemplateRef(),
                obligation.getOwnerId(),
                obligation.getStatus());
    }
}
