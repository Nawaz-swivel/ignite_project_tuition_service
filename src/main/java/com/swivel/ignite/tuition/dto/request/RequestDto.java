package com.swivel.ignite.tuition.dto.request;

import com.swivel.ignite.tuition.dto.BaseDto;

/**
 * RequestDto - All requestDto classes are needed to extend this class.
 */
public abstract class RequestDto implements BaseDto {

    /**
     * This method checks all required fields are available for a request.
     *
     * @return true/ false
     */
    public abstract boolean isRequiredAvailable();

    /**
     * This method checks the given field is non empty.
     *
     * @param field field
     * @return true/ false
     */
    protected boolean isNonEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }
}
