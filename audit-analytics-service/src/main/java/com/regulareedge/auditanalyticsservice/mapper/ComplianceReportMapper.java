package com.regulareedge.auditanalyticsservice.mapper;

import com.regulareedge.auditanalyticsservice.dto.response.ComplianceReportResponse;
import com.regulareedge.auditanalyticsservice.entity.ComplianceReport;

public final class ComplianceReportMapper {

    private ComplianceReportMapper() {
    }

    public static ComplianceReportResponse toResponse(ComplianceReport entity) {
        if (entity == null) {
            return null;
        }
        return new ComplianceReportResponse(
                entity.getReportId(),
                entity.getScope(),
                entity.getMetrics(),
                entity.getGeneratedDate());
    }
}
