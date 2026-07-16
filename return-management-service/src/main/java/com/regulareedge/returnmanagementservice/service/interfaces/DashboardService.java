package com.regulareedge.returnmanagementservice.service.interfaces;

import com.regulareedge.returnmanagementservice.dto.response.CcoDashboardResponse;

public interface DashboardService {

    /**
     * Aggregates pendingApprovals (RegulatoryReturn rows with status
     * PENDING_CCO_APPROVAL), openPenalties count and totalPenaltyAmount (PenaltyProceeding
     * rows with status OPEN), computed locally since both entities live in this service's
     * own database - no calls to other services are required.
     */
    CcoDashboardResponse getSummary();
}
