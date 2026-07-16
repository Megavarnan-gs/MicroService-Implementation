package com.regulareedge.auditanalyticsservice.exception;

/**
 * Raised when a call to a downstream microservice fails in a way that cannot be safely
 * ignored. Note: for aggregation reads used to build dashboards/reports, the architecture
 * favors fail-open (see ReturnManagementServiceClientFallback /
 * ComplianceCoreServiceClientFallback), so this exception is reserved for other downstream
 * integration failures.
 */
public class DownstreamServiceException extends RuntimeException {

    public DownstreamServiceException(String message) {
        super(message);
    }

    public DownstreamServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
