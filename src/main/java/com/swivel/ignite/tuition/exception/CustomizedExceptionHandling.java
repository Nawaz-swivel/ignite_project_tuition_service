package com.swivel.ignite.tuition.exception;

import com.swivel.ignite.tuition.enums.ErrorResponseStatusType;
import com.swivel.ignite.tuition.enums.ResponseStatusType;
import com.swivel.ignite.tuition.wrapper.ErrorResponseWrapper;
import com.swivel.ignite.tuition.wrapper.ResponseWrapper;
import com.swivel.ignite.tuition.wrapper.RestErrorResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";

    /**
     * This method returns an error response for tuition service exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(TuitionServiceException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionServiceException(TuitionServiceException exception) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method returns an error response for tuition not found exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(TuitionNotFoundException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionNotFoundException(TuitionNotFoundException exception) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .TUITION_NOT_FOUND.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType.TUITION_NOT_FOUND
                .getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method returns an error response for tuition already exists exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(TuitionAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionAlreadyExistsException(TuitionAlreadyExistsException exception) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .TUITION_ALREADY_EXISTS.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .TUITION_ALREADY_EXISTS.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method returns an error response for student microservice call exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(StudentServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handleStudentServiceHttpClientErrorException(
            StudentServiceHttpClientErrorException exception) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method returns an error response for payment microservice call exceptions
     *
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(PaymentServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handlePaymentServiceHttpClientErrorException(
            PaymentServiceHttpClientErrorException exception) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.PAYMENT_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.PAYMENT_INTERNAL_SERVER_ERROR.getCode());
        log.error(exception.getMessage());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
