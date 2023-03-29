package com.swivel.ignite.tuition.enums;

import lombok.Getter;

/**
 * Enum values for Error Response
 */
@Getter
public enum ErrorResponseStatusType {

    MISSING_REQUIRED_FIELDS(4001, "Missing required fields"),
    TUITION_ALREADY_EXISTS(4002, "Tuition with given name already exists"),
    STUDENT_ALREADY_EXISTS(4003, "Student already exists with given id"),
    TUITION_NOT_FOUND(4004, "Tuition not found"),
    STUDENT_NOT_FOUND(4005, "Student not found"),
    STUDENT_NOT_ENROLLED_IN_TUITION(4006, "Student not enrolled in tuition"),
    STUDENT_ALREADY_ENROLLED_IN_A_TUITION(4007, "Student already enrolled in a tuition"),
    USERNAME_PASSWORD_NOT_MATCH(4008, "Username and password do not match"),
    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    STUDENT_INTERNAL_SERVER_ERROR(5001, "Student Service - Internal Server Error"),
    PAYMENT_INTERNAL_SERVER_ERROR(5002, "Payment Service - Internal Server Error");

    private final int code;
    private final String message;

    ErrorResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
