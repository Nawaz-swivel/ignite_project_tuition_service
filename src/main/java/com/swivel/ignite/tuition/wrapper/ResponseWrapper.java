package com.swivel.ignite.tuition.wrapper;

import com.swivel.ignite.tuition.dto.BaseDto;
import com.swivel.ignite.tuition.enums.ResponseStatusType;
import lombok.Getter;

/**
 * Response wrapper
 */
@Getter
public class ResponseWrapper implements BaseDto {
    private final ResponseStatusType status;
    private final String message;
    private final String displayMessage;

    /**
     * @param status         status
     * @param message        dev message
     * @param displayMessage display message
     */
    public ResponseWrapper(ResponseStatusType status, String message, String displayMessage) {
        this.status = status;
        this.message = message;
        this.displayMessage = displayMessage;
    }

    @Override
    public String toLogJson() {
        return toJson();
    }
}
