package com.swivel.ignite.tuition.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.swivel.ignite.tuition.dto.response.ResponseDto;
import com.swivel.ignite.tuition.enums.ErrorResponseStatusType;
import com.swivel.ignite.tuition.enums.ResponseStatusType;
import com.swivel.ignite.tuition.enums.SuccessResponseStatusType;
import com.swivel.ignite.tuition.wrapper.ErrorResponseWrapper;
import com.swivel.ignite.tuition.wrapper.ResponseWrapper;
import com.swivel.ignite.tuition.wrapper.RestErrorResponseWrapper;
import com.swivel.ignite.tuition.wrapper.SuccessResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Base Controller
 */
public class Controller {

    protected static final String AUTH_HEADER = "Authorization";
    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";
    private static final String SUCCESS_MESSAGE = "Successfully returned the data.";

    /**
     * This method creates an empty data response for bad request scenarios
     *
     * @param status error status
     * @return bad request error response
     */
    protected ResponseEntity<ResponseWrapper> getBadRequestResponse(ErrorResponseStatusType status) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, status.getMessage(),
                null, ERROR_MESSAGE, status.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    /**
     * This method creates an empty data response for the internal server error scenarios
     *
     * @return internal server error response
     */
    protected ResponseEntity<ResponseWrapper> getInternalServerErrorResponse() {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method creates a response for rest api call errors
     *
     * @param status error status
     * @param data   rest api response
     * @return internal server error response
     */
    protected ResponseEntity<ResponseWrapper> getInternalServerErrorResponse(ErrorResponseStatusType status, JsonNode data) {
        ResponseWrapper responseWrapper = new RestErrorResponseWrapper(ResponseStatusType.ERROR, status.getMessage(),
                data, ERROR_MESSAGE, status.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This method creates data response for success scenarios
     *
     * @param status success status
     * @param data   response data
     * @return success response
     */
    protected ResponseEntity<ResponseWrapper> getSuccessResponse(SuccessResponseStatusType status, ResponseDto data) {
        ResponseWrapper responseWrapper = new SuccessResponseWrapper(ResponseStatusType.SUCCESS, status.getMessage(),
                data, SUCCESS_MESSAGE, status.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.valueOf(status.getCode()));
    }
}
