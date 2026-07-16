package com.regulareedge.compliancecoreservice.service.interfaces;

import com.regulareedge.compliancecoreservice.dto.request.ComplianceCalendarRequest;
import com.regulareedge.compliancecoreservice.dto.request.UpdateCalendarStatusRequest;
import com.regulareedge.compliancecoreservice.dto.response.ComplianceCalendarResponse;

import java.util.List;

public interface CalendarService {

    ComplianceCalendarResponse create(ComplianceCalendarRequest request);

    List<ComplianceCalendarResponse> getAll();

    List<ComplianceCalendarResponse> getUpcoming();

    List<ComplianceCalendarResponse> getOverdue();

    List<ComplianceCalendarResponse> getByObligation(int obligationId);

    ComplianceCalendarResponse updateStatus(UpdateCalendarStatusRequest request);
}
