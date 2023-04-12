package com.swivel.ignite.tuition.exception;

import com.swivel.ignite.tuition.enums.ErrorResponseStatusType;
import com.swivel.ignite.tuition.enums.ResponseStatusType;
import com.swivel.ignite.tuition.wrapper.ErrorResponseWrapper;
import com.swivel.ignite.tuition.wrapper.ResponseWrapper;
import com.swivel.ignite.tuition.wrapper.RestErrorResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";

    @ExceptionHandler(TuitionServiceException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionServiceException(TuitionServiceException exception,
                                                                         WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TuitionNotFoundException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionNotFoundException(TuitionNotFoundException exception,
                                                                          WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .TUITION_NOT_FOUND.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType.TUITION_NOT_FOUND
                .getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TuitionAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper> handleTuitionAlreadyExistsException(TuitionAlreadyExistsException exception,
                                                                               WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .TUITION_ALREADY_EXISTS.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .TUITION_ALREADY_EXISTS.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StudentServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handleStudentServiceHttpClientErrorException(
            StudentServiceHttpClientErrorException exception, WebRequest request) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PaymentServiceHttpClientErrorException.class)
    public ResponseEntity<ResponseWrapper> handlePaymentServiceHttpClientErrorException(
            PaymentServiceHttpClientErrorException exception, WebRequest request) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR,
                ErrorResponseStatusType.PAYMENT_INTERNAL_SERVER_ERROR.getMessage(), exception.responseBody,
                ERROR_MESSAGE, ErrorResponseStatusType.PAYMENT_INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
