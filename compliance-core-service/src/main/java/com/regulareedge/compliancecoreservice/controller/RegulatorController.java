package com.regulareedge.compliancecoreservice.controller;

import com.regulareedge.compliancecoreservice.dto.request.DeleteRegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.request.RegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.response.MessageResponse;
import com.regulareedge.compliancecoreservice.dto.response.RegulatorResponse;
import com.regulareedge.compliancecoreservice.service.interfaces.RegulatorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regulators")
@Tag(name = "Regulator Management")
public class RegulatorController {

    private final RegulatorService regulatorService;

    public RegulatorController(RegulatorService regulatorService) {
        this.regulatorService = regulatorService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegulatorResponse> add(@Valid @RequestBody RegulatorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regulatorService.create(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'CO')")
    public ResponseEntity<List<RegulatorResponse>> getAll() {
        return ResponseEntity.ok(regulatorService.getAll());
    }

    @GetMapping("/getActive")
    public ResponseEntity<List<RegulatorResponse>> getActive() {
        return ResponseEntity.ok(regulatorService.getActive());
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(@Valid @RequestBody DeleteRegulatorRequest request) {
        regulatorService.delete(request.getRegulatorId());
        return ResponseEntity.ok(new MessageResponse(
                "Regulator with id " + request.getRegulatorId() + " has been deleted"));
    }
}
