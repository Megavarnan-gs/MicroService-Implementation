package com.regulareedge.compliancecoreservice.controller;

import com.regulareedge.compliancecoreservice.dto.request.DataCertificationRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCertificationResponse;
import com.regulareedge.compliancecoreservice.service.interfaces.DataCertificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/certifications")
@Tag(name = "Data Certification Management")
public class DataCertificationController {

    private final DataCertificationService dataCertificationService;

    public DataCertificationController(DataCertificationService dataCertificationService) {
        this.dataCertificationService = dataCertificationService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('DATA_OWNER')")
    public ResponseEntity<DataCertificationResponse> add(@Valid @RequestBody DataCertificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataCertificationService.create(request));
    }

    @GetMapping("/byRequest/{requestId}")
    public ResponseEntity<List<DataCertificationResponse>> byRequest(@PathVariable int requestId) {
        return ResponseEntity.ok(dataCertificationService.getByRequest(requestId));
    }
}
