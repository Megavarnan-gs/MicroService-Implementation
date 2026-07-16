package com.regulareedge.returnmanagementservice.controller;

import com.regulareedge.returnmanagementservice.dto.request.SubmissionRecordRequest;
import com.regulareedge.returnmanagementservice.dto.response.SubmissionRecordResponse;
import com.regulareedge.returnmanagementservice.service.interfaces.SubmissionRecordService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@Validated
@Tag(name = "Submission Record Management")
public class SubmissionRecordController {

    private final SubmissionRecordService submissionRecordService;

    public SubmissionRecordController(SubmissionRecordService submissionRecordService) {
        this.submissionRecordService = submissionRecordService;
    }

    @PostMapping("/record")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<SubmissionRecordResponse> record(@Valid @RequestBody SubmissionRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionRecordService.record(request));
    }

    @GetMapping("/byReturn/{returnId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SubmissionRecordResponse>> byReturn(@PathVariable @Positive int returnId) {
        return ResponseEntity.ok(submissionRecordService.getByReturn(returnId));
    }
}
