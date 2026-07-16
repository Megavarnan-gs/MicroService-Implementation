package com.regulareedge.returnmanagementservice.exception;

/**
 * Raised when a call to a downstream microservice fails in a way that cannot be safely
 * ignored. Note: for user-id and cross-service reference validation (obligationId,
 * calendarId, regulatorId, user ids), the architecture favors fail-open (see
 * UserServiceClientFallback / ComplianceCoreServiceClientFallback), so this exception is
 * reserved for other downstream integration failures.
 */
public class DownstreamServiceException extends RuntimeException {

    public DownstreamServiceException(String message) {
        super(message);
    }

    public DownstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
