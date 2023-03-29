package com.swivel.ignite.tuition.exception;

/**
 * RegistrationServiceException
 */
public class TuitionServiceException extends RuntimeException {

    /**
     * RegistrationServiceException with error message.
     *
     * @param errorMessage error message
     */
    public TuitionServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * RegistrationServiceException with error message and throwable error
     *
     * @param errorMessage error message
     * @param error        error
     */
    public TuitionServiceException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
