package com.regulareedge.compliancecoreservice.controller;

import com.regulareedge.compliancecoreservice.dto.request.DataCollectionCreateRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateDataRequestStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.DataCollectionRequestResponse;
import com.regulareedge.compliancecoreservice.service.interfaces.DataCollectionService;
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
@RequestMapping("/data-requests")
@Tag(name = "Data Collection Request Management")
public class DataCollectionController {

    private final DataCollectionService dataCollectionService;

    public DataCollectionController(DataCollectionService dataCollectionService) {
        this.dataCollectionService = dataCollectionService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('CO')")
    public ResponseEntity<DataCollectionRequestResponse> create(
            @Valid @RequestBody DataCollectionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataCollectionService.create(request));
    }

    @GetMapping("/myRequests/{dataOwnerId}")
    @PreAuthorize("hasRole('DATA_OWNER')")
    public ResponseEntity<List<DataCollectionRequestResponse>> myRequests(@PathVariable int dataOwnerId) {
        return ResponseEntity.ok(dataCollectionService.getByDataOwner(dataOwnerId));
    }

    @PutMapping("/updateStatus")
    @PreAuthorize("hasRole('DATA_OWNER')")
    public ResponseEntity<DataCollectionRequestResponse> updateStatus(
            @Valid @RequestBody UpdateDataRequestStatusRequest request) {
        return ResponseEntity.ok(dataCollectionService.updateStatus(request));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'CO')")
    public ResponseEntity<List<DataCollectionRequestResponse>> getAll() {
        return ResponseEntity.ok(dataCollectionService.getAll());
    }
}
