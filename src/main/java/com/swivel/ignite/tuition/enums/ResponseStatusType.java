package com.swivel.ignite.tuition.enums;

/**
 * Enum values for Response status
 */
public enum ResponseStatusType {

    SUCCESS("Success"),
    ERROR("Error");

    private final String status;

    ResponseStatusType(String status) {
        this.status = status;
    }
}
