package com.regulareedge.compliancecoreservice.mapper;

import com.regulareedge.compliancecoreservice.dto.response.DataCertificationResponse;
import com.regulareedge.compliancecoreservice.entity.DataCertification;

public final class DataCertificationMapper {

    private DataCertificationMapper() {
    }

    public static DataCertificationResponse toResponse(DataCertification certification) {
        if (certification == null) {
            return null;
        }
        return new DataCertificationResponse(
                certification.getCertificationId(),
                certification.getRequestId(),
                certification.getCertifiedById(),
                certification.getCertificationDate(),
                certification.getCertificationStatement(),
                certification.getStatus());
    }
}
