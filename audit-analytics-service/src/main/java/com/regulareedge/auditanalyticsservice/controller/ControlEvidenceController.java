package com.regulareedge.auditanalyticsservice.controller;

import com.regulareedge.auditanalyticsservice.dto.request.ControlEvidenceRequest;
import com.regulareedge.auditanalyticsservice.dto.request.UpdateEvidenceStatusRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ControlEvidenceResponse;
import com.regulareedge.auditanalyticsservice.service.interfaces.ControlEvidenceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/control-evidence")
@Validated
@Tag(name = "Control Evidence Management")
public class ControlEvidenceController {

    private final ControlEvidenceService controlEvidenceService;

    public ControlEvidenceController(ControlEvidenceService controlEvidenceService) {
        this.controlEvidenceService = controlEvidenceService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('AUDITOR', 'CO')")
    public ResponseEntity<ControlEvidenceResponse> upload(@Valid @RequestBody ControlEvidenceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(controlEvidenceService.upload(request));
    }

    @GetMapping("/byReturn/{returnId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ControlEvidenceResponse>> byReturn(@PathVariable @Positive int returnId) {
        return ResponseEntity.ok(controlEvidenceService.getByReturn(returnId));
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('AUDITOR')")
    public ResponseEntity<ControlEvidenceResponse> updateStatus(
            @Valid @RequestBody UpdateEvidenceStatusRequest request) {
        return ResponseEntity.ok(controlEvidenceService.updateStatus(request));
    }
}
