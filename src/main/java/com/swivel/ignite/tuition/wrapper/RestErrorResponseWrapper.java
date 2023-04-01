package com.swivel.ignite.tuition.wrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.swivel.ignite.tuition.enums.ResponseStatusType;
import lombok.Getter;

/**
 * Rest Error response wrapper
 */
@Getter
public class RestErrorResponseWrapper extends ResponseWrapper {

    private final transient JsonNode data;
    private final int errorCode;

    /**
     * @param status         status
     * @param message        dev message
     * @param data           data
     * @param displayMessage display message
     * @param errorCode      error code
     */
    public RestErrorResponseWrapper(ResponseStatusType status, String message, JsonNode data, String displayMessage,
                                    int errorCode) {
        super(status, message, displayMessage);
        this.data = data;
        this.errorCode = errorCode;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
