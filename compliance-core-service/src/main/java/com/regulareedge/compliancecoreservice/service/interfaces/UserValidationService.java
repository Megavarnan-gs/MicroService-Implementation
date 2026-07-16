package com.regulareedge.compliancecoreservice.service.interfaces;

public interface UserValidationService {

    /**
     * Checks (best-effort) whether a given external user id is known to auth-service.
     * Because the backing auth-service endpoint does not exist yet, this call is
     * resilience4j-guarded and fails open (returns true) on any downstream failure.
     */
    boolean userExists(int userId);
}
