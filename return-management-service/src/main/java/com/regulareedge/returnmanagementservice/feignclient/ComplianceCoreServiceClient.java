package com.regulareedge.returnmanagementservice.feignclient;

import com.regulareedge.returnmanagementservice.config.FeignConfig;
import com.regulareedge.returnmanagementservice.dto.response.CalendarRefResponse;
import com.regulareedge.returnmanagementservice.dto.response.ObligationRefResponse;
import com.regulareedge.returnmanagementservice.dto.response.RegulatorRefResponse;
import com.regulareedge.returnmanagementservice.feignclient.fallback.ComplianceCoreServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client for compliance-core-service's real, already-implemented endpoints, used to
 * best-effort validate obligationId/calendarId/regulatorId references supplied on
 * return-management-service requests. Full synchronous referential-integrity checking
 * across services is NOT a hard requirement at this stage - see
 * ComplianceCoreServiceClientFallback for the fail-open behaviour applied when
 * compliance-core-service is unreachable or slow.
 */
@FeignClient(name = "compliance-core-service", configuration = FeignConfig.class,
        fallback = ComplianceCoreServiceClientFallback.class)
public interface ComplianceCoreServiceClient {

    @GetMapping("/obligations/getAll")
    List<ObligationRefResponse> getAllObligations();

    @GetMapping("/calendar/byObligation/{obligationId}")
    List<CalendarRefResponse> getCalendarByObligation(@PathVariable("obligationId") int obligationId);

    @GetMapping("/regulators/getActive")
    List<RegulatorRefResponse> getActiveRegulators();
}
