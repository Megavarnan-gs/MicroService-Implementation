package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.RegulatorResponse;
import com.regulareedge.compliancecoreservice.entity.Regulator;

public final class RegulatorMapper {

    private RegulatorMapper() {
    }

    public static RegulatorResponse toResponse(Regulator regulator) {
        if (regulator == null) {
            return null;
        }
        return new RegulatorResponse(
                regulator.getRegulatorId(),
                regulator.getName(),
                regulator.getJurisdiction(),
                regulator.getRegulatoryDomain(),
                regulator.getContactDetails(),
                regulator.getStatus());
    }
}
