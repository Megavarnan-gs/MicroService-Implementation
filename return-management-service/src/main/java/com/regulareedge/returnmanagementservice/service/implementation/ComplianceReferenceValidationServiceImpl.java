package com.regulareedge.returnmanagementservice.service.implementation;

import com.regulareedge.returnmanagementservice.dto.response.CalendarRefResponse;
import com.regulareedge.returnmanagementservice.dto.response.ObligationRefResponse;
import com.regulareedge.returnmanagementservice.dto.response.RegulatorRefResponse;
import com.regulareedge.returnmanagementservice.feignclient.ComplianceCoreServiceClient;
import com.regulareedge.returnmanagementservice.service.interfaces.ComplianceReferenceValidationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Best-effort validation of obligationId/calendarId/regulatorId references against
 * compliance-core-service's real endpoints (GET /obligations/getAll,
 * GET /calendar/byObligation/{obligationId}, GET /regulators/getActive). Full synchronous
 * referential-integrity checking across services is not a hard blocker at this stage:
 * both the Feign fallback and the resilience4j fallbackMethod below fail open (report the
 * reference as "could not verify, so allow it") whenever compliance-core-service is
 * unreachable, slow, or its circuit breaker is open.
 */
@Service
public class ComplianceReferenceValidationServiceImpl implements ComplianceReferenceValidationService {

    private static final Logger logger = LoggerFactory.getLogger(ComplianceReferenceValidationServiceImpl.class);

    private final ComplianceCoreServiceClient complianceCoreServiceClient;

    public ComplianceReferenceValidationServiceImpl(ComplianceCoreServiceClient complianceCoreServiceClient) {
        this.complianceCoreServiceClient = complianceCoreServiceClient;
    }

    @Override
    @CircuitBreaker(name = "complianceCoreService", fallbackMethod = "obligationExistsFallback")
    @Retry(name = "complianceCoreService")
    public boolean obligationExists(int obligationId) {
        List<ObligationRefResponse> obligations = complianceCoreServiceClient.getAllObligations();
        if (obligations == null || obligations.isEmpty()) {
            // Empty list can legitimately mean "no obligations yet" OR "fallback triggered" -
            // in both cases we fail open rather than reject the request.
            return true;
        }
        return obligations.stream().anyMatch(o -> o.getObligationId() == obligationId);
    }

    @SuppressWarnings("unused")
    private boolean obligationExistsFallback(int obligationId, Throwable throwable) {
        logger.warn("obligationExists check failed for obligationId={} ({}). Failing open by design.",
                obligationId, throwable.getMessage());
        return true;
    }

    @Override
    @CircuitBreaker(name = "complianceCoreService", fallbackMethod = "calendarExistsFallback")
    @Retry(name = "complianceCoreService")
    public boolean calendarExists(int obligationId, int calendarId) {
        List<CalendarRefResponse> calendars = complianceCoreServiceClient.getCalendarByObligation(obligationId);
        if (calendars == null || calendars.isEmpty()) {
            return true;
        }
        return calendars.stream().anyMatch(c -> c.getCalendarId() == calendarId);
    }

    @SuppressWarnings("unused")
    private boolean calendarExistsFallback(int obligationId, int calendarId, Throwable throwable) {
        logger.warn("calendarExists check failed for obligationId={}, calendarId={} ({}). Failing open by design.",
                obligationId, calendarId, throwable.getMessage());
        return true;
    }

    @Override
    @CircuitBreaker(name = "complianceCoreService", fallbackMethod = "regulatorExistsFallback")
    @Retry(name = "complianceCoreService")
    public boolean regulatorExists(int regulatorId) {
        List<RegulatorRefResponse> regulators = complianceCoreServiceClient.getActiveRegulators();
        if (regulators == null || regulators.isEmpty()) {
            return true;
        }
        return regulators.stream().anyMatch(r -> r.getRegulatorId() == regulatorId);
    }

    @SuppressWarnings("unused")
    private boolean regulatorExistsFallback(int regulatorId, Throwable throwable) {
        logger.warn("regulatorExists check failed for regulatorId={} ({}). Failing open by design.",
                regulatorId, throwable.getMessage());
        return true;
    }
}
