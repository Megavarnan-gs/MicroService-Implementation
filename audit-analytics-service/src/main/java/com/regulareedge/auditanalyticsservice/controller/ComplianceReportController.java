package com.regulareedge.auditanalyticsservice.controller;

import com.regulareedge.auditanalyticsservice.common.enums.ReportScope;
import com.regulareedge.auditanalyticsservice.dto.request.GenerateReportRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ComplianceReportResponse;
import com.regulareedge.auditanalyticsservice.service.interfaces.ComplianceReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics/reports")
@Validated
@Tag(name = "Compliance Report Management")
public class ComplianceReportController {

    private final ComplianceReportService complianceReportService;

    public ComplianceReportController(ComplianceReportService complianceReportService) {
        this.complianceReportService = complianceReportService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<ComplianceReportResponse> generate(@Valid @RequestBody GenerateReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(complianceReportService.generate(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<List<ComplianceReportResponse>> getAll() {
        return ResponseEntity.ok(complianceReportService.getAll());
    }

    @GetMapping("/byScope/{scope}")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<List<ComplianceReportResponse>> byScope(@PathVariable @NotNull ReportScope scope) {
        return ResponseEntity.ok(complianceReportService.getByScope(scope));
    }
}
