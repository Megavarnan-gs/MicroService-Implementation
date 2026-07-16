package com.regulareedge.returnmanagementservice.controller;

import com.regulareedge.returnmanagementservice.dto.request.AssignCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.RegulatoryCorrespondenceRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdateCorrespondenceStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.RegulatoryCorrespondenceResponse;
import com.regulareedge.returnmanagementservice.service.interfaces.CorrespondenceService;
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
@RequestMapping("/correspondence")
@Validated
@Tag(name = "Regulatory Correspondence Management")
public class CorrespondenceController {

    private final CorrespondenceService correspondenceService;

    public CorrespondenceController(CorrespondenceService correspondenceService) {
        this.correspondenceService = correspondenceService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('LEGAL', 'CO')")
    public ResponseEntity<RegulatoryCorrespondenceResponse> create(
            @Valid @RequestBody RegulatoryCorrespondenceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(correspondenceService.create(request));
    }

    @GetMapping("/byRegulator/{regulatorId}")
    @PreAuthorize("hasRole('LEGAL')")
    public ResponseEntity<List<RegulatoryCorrespondenceResponse>> byRegulator(
            @PathVariable @Positive int regulatorId) {
        return ResponseEntity.ok(correspondenceService.getByRegulator(regulatorId));
    }

    @PutMapping("/assign")
    @PreAuthorize("hasRole('LEGAL')")
    public ResponseEntity<RegulatoryCorrespondenceResponse> assign(
            @Valid @RequestBody AssignCorrespondenceRequest request) {
        return ResponseEntity.ok(correspondenceService.assign(request));
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('LEGAL')")
    public ResponseEntity<RegulatoryCorrespondenceResponse> updateStatus(
            @Valid @RequestBody UpdateCorrespondenceStatusRequest request) {
        return ResponseEntity.ok(correspondenceService.updateStatus(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('LEGAL', 'ADMIN')")
    public ResponseEntity<List<RegulatoryCorrespondenceResponse>> getAll() {
        return ResponseEntity.ok(correspondenceService.getAll());
    }
}
