package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.dto.request.SubmissionRecordRequest;
import com.regulareedge.returnmanagementservice.dto.response.SubmissionRecordResponse;

import java.util.List;

public interface SubmissionRecordService {

    SubmissionRecordResponse record(SubmissionRecordRequest request);

    List<SubmissionRecordResponse> getByReturn(int returnId);
}
