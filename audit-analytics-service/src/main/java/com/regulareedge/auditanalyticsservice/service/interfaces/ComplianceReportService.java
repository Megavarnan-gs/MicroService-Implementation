package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.common.enums.ReportScope;
import com.regulareedge.auditanalyticsservice.dto.request.GenerateReportRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ComplianceReportResponse;

import java.util.List;

public interface ComplianceReportService {

    ComplianceReportResponse generate(GenerateReportRequest request);

    List<ComplianceReportResponse> getAll();

    List<ComplianceReportResponse> getByScope(ReportScope scope);
}
