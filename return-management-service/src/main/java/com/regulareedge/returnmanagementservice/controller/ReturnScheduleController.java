package com.regulareedge.returnmanagementservice.controller;

import com.regulareedge.returnmanagementservice.dto.request.ReturnScheduleRequest;
import com.regulareedge.returnmanagementservice.dto.response.ReturnScheduleResponse;
import com.regulareedge.returnmanagementservice.service.interfaces.ReturnScheduleService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@Validated
@Tag(name = "Return Schedule Management")
public class ReturnScheduleController {

    private final ReturnScheduleService returnScheduleService;

    public ReturnScheduleController(ReturnScheduleService returnScheduleService) {
        this.returnScheduleService = returnScheduleService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<ReturnScheduleResponse> add(@Valid @RequestBody ReturnScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(returnScheduleService.add(request));
    }

    @GetMapping("/byReturn/{returnId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReturnScheduleResponse>> byReturn(@PathVariable @Positive int returnId) {
        return ResponseEntity.ok(returnScheduleService.getByReturn(returnId));
    }

    @PutMapping("/validate/{scheduleId}")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<ReturnScheduleResponse> validate(@PathVariable @Positive int scheduleId,
                                                            @RequestParam @Positive int validatedById) {
        return ResponseEntity.ok(returnScheduleService.validate(scheduleId, validatedById));
    }
}
