package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.dto.request.ReturnScheduleRequest;
import com.regulareedge.returnmanagementservice.dto.response.ReturnScheduleResponse;

import java.util.List;

public interface ReturnScheduleService {

    ReturnScheduleResponse add(ReturnScheduleRequest request);

    List<ReturnScheduleResponse> getByReturn(int returnId);

    ReturnScheduleResponse validate(int scheduleId, int validatedById);
}
