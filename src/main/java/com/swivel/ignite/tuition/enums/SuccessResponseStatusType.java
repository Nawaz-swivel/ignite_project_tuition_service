package com.swivel.ignite.tuition.enums;

import lombok.Getter;

/**
 * Enum values for Success Responses
 */
@Getter
public enum SuccessResponseStatusType {

    CREATE_TUITION(201, "Successfully created the tuition"),
    ADD_TUITION_STUDENT(200, "Successfully added student to tuition"),
    READ_TUITION(200, "Successfully read the tuition"),
    DELETE_TUITION(202, "Successfully deleted the tuition"),
    REMOVE_TUITION_STUDENT(200, "Successfully removed student from tuition"),
    RETURNED_ALL_TUITION(200, "Successfully returned tuition list");

    private final int code;
    private final String message;

    SuccessResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
