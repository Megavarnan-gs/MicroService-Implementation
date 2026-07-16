package com.regulareedge.compliancecoreservice.service.interfaces;

import com.regulareedge.compliancecoreservice.dto.request.RegulatorRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatorResponse;

import java.util.List;

public interface RegulatorService {

    RegulatorResponse create(RegulatorRequest request);

    List<RegulatorResponse> getAll();

    List<RegulatorResponse> getActive();

    void delete(int regulatorId);
}
