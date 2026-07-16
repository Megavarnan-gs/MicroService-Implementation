package com.regulareedge.returnmanagementservice.service.interfaces;

public interface ComplianceReferenceValidationService {

    /**
     * Best-effort check that an obligationId is known to compliance-core-service.
     * Returns true if the id is confirmed OR if compliance-core-service could not be
     * reached (fail-open) - see ComplianceCoreServiceClientFallback.
     */
    boolean obligationExists(int obligationId);

    /**
     * Best-effort check that a calendarId is known to compliance-core-service for the
     * given obligationId. Fails open in the same way as {@link #obligationExists(int)}.
     */
    boolean calendarExists(int obligationId, int calendarId);

    /**
     * Best-effort check that a regulatorId corresponds to an ACTIVE regulator known to
     * compliance-core-service. Fails open in the same way as {@link #obligationExists(int)}.
     */
    boolean regulatorExists(int regulatorId);
}
