package com.regulareedge.compliancecoreservice.controller;

import com.regulareedge.compliancecoreservice.dto.request.ComplianceCalendarRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateCalendarStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.ComplianceCalendarResponse;
import com.regulareedge.compliancecoreservice.service.interfaces.CalendarService;
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
@RequestMapping("/calendar")
@Tag(name = "Compliance Calendar Management")
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComplianceCalendarResponse> add(@Valid @RequestBody ComplianceCalendarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(calendarService.create(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ComplianceCalendarResponse>> getAll() {
        return ResponseEntity.ok(calendarService.getAll());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ComplianceCalendarResponse>> upcoming() {
        return ResponseEntity.ok(calendarService.getUpcoming());
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<ComplianceCalendarResponse>> overdue() {
        return ResponseEntity.ok(calendarService.getOverdue());
    }

    @GetMapping("/byObligation/{obligationId}")
    public ResponseEntity<List<ComplianceCalendarResponse>> byObligation(@PathVariable int obligationId) {
        return ResponseEntity.ok(calendarService.getByObligation(obligationId));
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ComplianceCalendarResponse> updateStatus(
            @Valid @RequestBody UpdateCalendarStatusRequest request) {
        return ResponseEntity.ok(calendarService.updateStatus(request));
    }
}
