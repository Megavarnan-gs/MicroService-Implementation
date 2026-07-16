package com.regulareedge.returnmanagementservice.common.enums;

/**
 * ReturnSchedule.status is persisted as a plain String column, matching the monolith
 * convention for this entity. This enum is used only for service-layer validation of
 * incoming status values before persistence.
 */
public enum ScheduleStatus {
    DRAFT,
    VALIDATED,
    REJECTED
}
