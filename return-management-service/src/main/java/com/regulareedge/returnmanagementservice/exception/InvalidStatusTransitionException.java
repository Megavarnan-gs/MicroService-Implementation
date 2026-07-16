package com.regulareedge.returnmanagementservice.exception;

/**
 * Raised when an update request supplies a status value that is not one of the values
 * accepted for the target entity (validated against the corresponding enum in
 * common.enums), or that represents an invalid transition from the entity's current
 * status. Entities themselves persist status as a plain String column - see the entity
 * classes for details.
 */
public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
