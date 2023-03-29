package com.swivel.ignite.tuition.exception;

/**
 * Tuition Not Found Exception
 */
public class TuitionNotFoundException extends RuntimeException {

    /**
     * Tuition Not Found Exception with error message.
     *
     * @param errorMessage error message
     */
    public TuitionNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Tuition Not Found Exception with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public TuitionNotFoundException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
