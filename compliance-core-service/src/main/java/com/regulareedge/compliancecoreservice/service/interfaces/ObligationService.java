package com.regulareedge.compliancecoreservice.service.interfaces;

import com.regulareedge.compliancecoreservice.dto.request.RegulatoryObligationRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateObligationStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.RegulatoryObligationResponse;

import java.util.List;

public interface ObligationService {

    RegulatoryObligationResponse create(RegulatoryObligationRequest request);

    List<RegulatoryObligationResponse> getAll();

    List<RegulatoryObligationResponse> getActive();

    List<RegulatoryObligationResponse> getByRegulator(int regulatorId);

    List<RegulatoryObligationResponse> getByOwner(int ownerId);

    RegulatoryObligationResponse updateStatus(UpdateObligationStatusRequest request);
}
