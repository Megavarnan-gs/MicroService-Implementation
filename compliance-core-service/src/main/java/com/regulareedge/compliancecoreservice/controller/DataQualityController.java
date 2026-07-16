package com.regulareedge.compliancecoreservice.controller;

import com.regulareedge.compliancecoreservice.dto.request.DataQualityCheckRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataQualityCheckResponse;
import com.regulareedge.compliancecoreservice.service.interfaces.DataQualityService;
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
@RequestMapping("/data-quality")
@Tag(name = "Data Quality Check Management")
public class DataQualityController {

    private final DataQualityService dataQualityService;

    public DataQualityController(DataQualityService dataQualityService) {
        this.dataQualityService = dataQualityService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('DATA_OWNER', 'CO')")
    public ResponseEntity<DataQualityCheckResponse> add(@Valid @RequestBody DataQualityCheckRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataQualityService.create(request));
    }

    @GetMapping("/byRequest/{requestId}")
    public ResponseEntity<List<DataQualityCheckResponse>> byRequest(@PathVariable int requestId) {
        return ResponseEntity.ok(dataQualityService.getByRequest(requestId));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DataQualityCheckResponse>> getAll() {
        return ResponseEntity.ok(dataQualityService.getAll());
    }
}
