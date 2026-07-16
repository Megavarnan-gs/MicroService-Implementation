package com.regulareedge.auditanalyticsservice.service.interfaces;

import com.regulareedge.auditanalyticsservice.dto.request.ControlEvidenceRequest;
import com.regulareedge.auditanalyticsservice.dto.request.UpdateEvidenceStatusRequest;
import com.regulareedge.auditanalyticsservice.dto.response.ControlEvidenceResponse;

import java.util.List;

public interface ControlEvidenceService {

    ControlEvidenceResponse upload(ControlEvidenceRequest request);

    List<ControlEvidenceResponse> getByReturn(int returnId);

    ControlEvidenceResponse updateStatus(UpdateEvidenceStatusRequest request);
}
