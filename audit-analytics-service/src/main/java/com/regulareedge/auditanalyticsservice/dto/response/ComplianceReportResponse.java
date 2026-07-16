package com.regulareedge.auditanalyticsservice.dto.response;

import com.regulareedge.auditanalyticsservice.common.enums.ReportScope;

import java.time.LocalDate;

public class ComplianceReportResponse {

    private int reportId;
    private ReportScope scope;
    private String metrics;
    private LocalDate generatedDate;

    public ComplianceReportResponse() {
    }

    public ComplianceReportResponse(int reportId, ReportScope scope, String metrics, LocalDate generatedDate) {
        this.reportId = reportId;
        this.scope = scope;
        this.metrics = metrics;
        this.generatedDate = generatedDate;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public ReportScope getScope() {
        return scope;
    }

    public void setScope(ReportScope scope) {
        this.scope = scope;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public LocalDate getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(LocalDate generatedDate) {
        this.generatedDate = generatedDate;
    }
}
