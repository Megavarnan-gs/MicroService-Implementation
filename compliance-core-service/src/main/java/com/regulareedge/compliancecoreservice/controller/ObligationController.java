package com.regulareedge.compliancecoreservice.controller;

import com.regulareedge.compliancecoreservice.dto.request.RegulatoryObligationRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateObligationStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatoryObligationResponse;
import com.regulareedge.compliancecoreservice.service.interfaces.ObligationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/obligations")
@Tag(name = "Regulatory Obligation Management")
public class ObligationController {

    private final ObligationService obligationService;

    public ObligationController(ObligationService obligationService) {
        this.obligationService = obligationService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegulatoryObligationResponse> add(@Valid @RequestBody RegulatoryObligationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(obligationService.create(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegulatoryObligationResponse>> getAll() {
        return ResponseEntity.ok(obligationService.getAll());
    }

    @GetMapping("/getActive")
    public ResponseEntity<List<RegulatoryObligationResponse>> getActive() {
        return ResponseEntity.ok(obligationService.getActive());
    }

    @GetMapping("/byRegulator/{regulatorId}")
    public ResponseEntity<List<RegulatoryObligationResponse>> byRegulator(@PathVariable int regulatorId) {
        return ResponseEntity.ok(obligationService.getByRegulator(regulatorId));
    }

    @GetMapping("/myObligations/{ownerId}")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<List<RegulatoryObligationResponse>> myObligations(@PathVariable int ownerId) {
        return ResponseEntity.ok(obligationService.getByOwner(ownerId));
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegulatoryObligationResponse> updateStatus(
            @Valid @RequestBody UpdateObligationStatusRequest request) {
        return ResponseEntity.ok(obligationService.updateStatus(request));
    }
}
