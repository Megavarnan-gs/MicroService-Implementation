package com.regulareedge.auditanalyticsservice.dto.request;

import com.regulareedge.auditanalyticsservice.common.enums.ReportScope;
import jakarta.validation.constraints.NotNull;

public class GenerateReportRequest {

    @NotNull
    private ReportScope scope;

    public ReportScope getScope() {
        return scope;
    }

    public void setScope(ReportScope scope) {
        this.scope = scope;
    }
}
