package com.swivel.ignite.tuition.exception;

/**
 * Tuition Already Exists Exception
 */
public class TuitionAlreadyExistsException extends RuntimeException {

    /**
     * Tuition Already Exists Exception with error message.
     *
     * @param errorMessage error message
     */
    public TuitionAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Tuition Already Exists Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public TuitionAlreadyExistsException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
