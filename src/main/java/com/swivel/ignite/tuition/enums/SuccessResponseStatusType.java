package com.swivel.ignite.tuition.enums;

import lombok.Getter;

/**
 * Enum values for Success Response
 */
@Getter
public enum SuccessResponseStatusType {

    CREATE_TUITION(2000, "Successfully created the tuition"),
    CREATE_STUDENT(2001, "Successfully created the student"),
    ADD_TUITION_STUDENT(2002, "Successfully added student to tuition"),
    READ_TUITION(2003, "Successfully read the tuition"),
    DELETE_TUITION(2004, "Successfully deleted the tuition"),
    DELETE_STUDENT(2005, "Successfully deleted the student"),
    GET_STUDENT(2006, "Successfully retrieved the student"),
    REMOVE_TUITION_STUDENT(2007, "Successfully removed student from tuition"),
    RETURNED_ALL_TUITION(2008, "Successfully returned tuition list"),
    RETURNED_ALL_STUDENT(2009, "Successfully returned students list"),
    LOGIN_STUDENT(2010, "Successfully logged in the student");

    private final int code;
    private final String message;

    SuccessResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
