package com.regulareedge.returnmanagementservice.controller;

import com.regulareedge.returnmanagementservice.dto.request.PenaltyProceedingRequest;
import com.regulareedge.returnmanagementservice.dto.request.UpdatePenaltyStatusRequest;
import com.regulareedge.returnmanagementservice.dto.response.PenaltyProceedingResponse;
import com.regulareedge.returnmanagementservice.service.interfaces.PenaltyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/penalties")
@Validated
@Tag(name = "Penalty Proceeding Management")
public class PenaltyController {

    private final PenaltyService penaltyService;

    public PenaltyController(PenaltyService penaltyService) {
        this.penaltyService = penaltyService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('LEGAL')")
    public ResponseEntity<PenaltyProceedingResponse> add(@Valid @RequestBody PenaltyProceedingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(penaltyService.add(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('LEGAL', 'CCO')")
    public ResponseEntity<List<PenaltyProceedingResponse>> getAll() {
        return ResponseEntity.ok(penaltyService.getAll());
    }

    @GetMapping("/getByStatus")
    @PreAuthorize("hasAnyRole('LEGAL', 'CCO')")
    public ResponseEntity<List<PenaltyProceedingResponse>> getByStatus(@RequestParam @NotBlank String status) {
        return ResponseEntity.ok(penaltyService.getByStatus(status));
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('LEGAL')")
    public ResponseEntity<PenaltyProceedingResponse> updateStatus(
            @Valid @RequestBody UpdatePenaltyStatusRequest request) {
        return ResponseEntity.ok(penaltyService.updateStatus(request));
    }
}
