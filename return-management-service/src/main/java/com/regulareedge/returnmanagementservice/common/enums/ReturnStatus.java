package com.regulareedge.returnmanagementservice.common.enums;

/**
 * RegulatoryReturn.status is persisted as a plain String column (matching the monolith
 * comment "status String NOT enum - this fixes the 500 error"), so this enum is used ONLY
 * in the service layer to validate incoming status values before they are persisted.
 */
public enum ReturnStatus {
    DRAFT,
    IN_PROGRESS,
    PENDING_CCO_APPROVAL,
    APPROVED,
    SUBMITTED,
    REJECTED
}
