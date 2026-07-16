package com.regulareedge.returnmanagementservice.feignclient.fallback;

import com.regulareedge.returnmanagementservice.dto.response.CalendarRefResponse;
import com.regulareedge.returnmanagementservice.dto.response.ObligationRefResponse;
import com.regulareedge.returnmanagementservice.dto.response.RegulatorRefResponse;
import com.regulareedge.returnmanagementservice.feignclient.ComplianceCoreServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fail-open fallback for ComplianceCoreServiceClient. Cross-service referential-integrity
 * checks (obligationId/calendarId/regulatorId) are best-effort: if compliance-core-service
 * is unreachable, slow, or its circuit breaker is open, this fallback returns an empty
 * list rather than blocking the caller. Callers must treat an empty list as "could not
 * verify" and proceed rather than reject the request outright, per architecture decision.
 * Every fallback invocation is logged at WARN so the gap stays visible.
 */
@Component
public class ComplianceCoreServiceClientFallback implements ComplianceCoreServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ComplianceCoreServiceClientFallback.class);

    @Override
    public List<ObligationRefResponse> getAllObligations() {
        logger.warn("Falling back for compliance-core-service getAllObligations(). Failing open by design - "
                + "obligationId references will not be verified for this request.");
        return List.of();
    }

    @Override
    public List<CalendarRefResponse> getCalendarByObligation(int obligationId) {
        logger.warn("Falling back for compliance-core-service getCalendarByObligation(obligationId={}). "
                + "Failing open by design - calendarId references will not be verified for this request.",
                obligationId);
        return List.of();
    }

    @Override
    public List<RegulatorRefResponse> getActiveRegulators() {
        logger.warn("Falling back for compliance-core-service getActiveRegulators(). Failing open by design - "
                + "regulatorId references will not be verified for this request.");
        return List.of();
    }
}
