package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.common.enums.PenaltyStatus;
import com.regulareedge.returnmanagementservice.common.enums.ReturnStatus;
import com.regulareedge.returnmanagementservice.dto.response.CcoDashboardResponse;
import com.regulareedge.returnmanagementservice.entity.PenaltyProceeding;
import com.regulareedge.returnmanagementservice.repository.RegulatoryReturnRepository;
import com.regulareedge.returnmanagementservice.service.interfaces.DashboardService;
import com.regulareedge.returnmanagementservice.service.interfaces.PenaltyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Mirrors the monolith's CCOController#getDashboard aggregation, but computed purely
 * from this service's own RegulatoryReturn and PenaltyProceeding tables - no calls to
 * other microservices are needed since both entities live in this service's database.
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final RegulatoryReturnRepository regulatoryReturnRepository;
    private final PenaltyService penaltyService;

    public DashboardServiceImpl(RegulatoryReturnRepository regulatoryReturnRepository, PenaltyService penaltyService) {
        this.regulatoryReturnRepository = regulatoryReturnRepository;
        this.penaltyService = penaltyService;
    }

    @Override
    public CcoDashboardResponse getSummary() {
        int pendingApprovals = regulatoryReturnRepository
                .findByStatus(ReturnStatus.PENDING_CCO_APPROVAL.name())
                .size();

        List<PenaltyProceeding> openPenalties = penaltyService.getEntitiesByStatus(PenaltyStatus.OPEN);
        double totalPenaltyAmount = openPenalties.stream()
                .mapToDouble(PenaltyProceeding::getPenaltyAmount)
                .sum();

        return new CcoDashboardResponse(pendingApprovals, openPenalties.size(), totalPenaltyAmount);
    }
}
