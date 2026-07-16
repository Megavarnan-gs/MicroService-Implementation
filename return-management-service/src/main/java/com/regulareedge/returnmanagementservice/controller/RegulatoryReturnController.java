package com.regulareedge.returnmanagementservice.controller;

import com.regulareedge.returnmanagementservice.dto.request.RegulatoryReturnRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateReturnStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.CcoDashboardResponse;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryReturnResponse;
import com.regulareedge.returnmanagementservice.service.interfaces.DashboardService;
import com.regulareedge.returnmanagementservice.service.interfaces.RegulatoryReturnService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/returns")
@Validated
@Tag(name = "Regulatory Return Management")
public class RegulatoryReturnController {

    private final RegulatoryReturnService regulatoryReturnService;
    private final DashboardService dashboardService;

    public RegulatoryReturnController(RegulatoryReturnService regulatoryReturnService,
                                       DashboardService dashboardService) {
        this.regulatoryReturnService = regulatoryReturnService;
        this.dashboardService = dashboardService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<RegulatoryReturnResponse> create(@Valid @RequestBody RegulatoryReturnRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regulatoryReturnService.create(request));
    }

    @GetMapping("/myReturns/{userId}")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<List<RegulatoryReturnResponse>> myReturns(@PathVariable @Positive int userId) {
        return ResponseEntity.ok(regulatoryReturnService.getMyReturns(userId));
    }

    @GetMapping("/getByStatus")
    @PreAuthorize("hasAnyRole('CO', 'CCO')")
    public ResponseEntity<List<RegulatoryReturnResponse>> getByStatus(
            @RequestParam @NotBlank String status) {
        return ResponseEntity.ok(regulatoryReturnService.getByStatus(status));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'CCO')")
    public ResponseEntity<List<RegulatoryReturnResponse>> getAll() {
        return ResponseEntity.ok(regulatoryReturnService.getAll());
    }

    @GetMapping("/pendingApprovals")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<List<RegulatoryReturnResponse>> pendingApprovals() {
        return ResponseEntity.ok(regulatoryReturnService.getPendingApprovals());
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasAnyRole('CO', 'CCO')")
    public ResponseEntity<RegulatoryReturnResponse> updateStatus(
            @Valid @RequestBody UpdateReturnStatusRequest request) {
        return ResponseEntity.ok(regulatoryReturnService.updateStatus(request));
    }

    @GetMapping("/dashboard-summary")
    @PreAuthorize("hasRole('CCO')")
    public ResponseEntity<CcoDashboardResponse> dashboardSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }
}
