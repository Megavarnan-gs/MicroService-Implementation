package com.regulareedge.compliancecoreservice.exception;

/**
 * Raised when a call to a downstream microservice (e.g. auth-service) fails in a way
 * that cannot be safely ignored. Note: for the specific case of user-id existence checks,
 * the architecture favors fail-open (see UserServiceClientFallback), so this exception is
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
