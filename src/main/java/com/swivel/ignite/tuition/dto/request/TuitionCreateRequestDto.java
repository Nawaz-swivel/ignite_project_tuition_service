package com.swivel.ignite.tuition.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for Tuition creation request
 */
@Getter
@Setter
public class TuitionCreateRequestDto extends RequestDto {

    private String name;
    private String location;

    @Override
    public String toLogJson() {
        return null;
    }

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(name) && isNonEmpty(location);
    }
}
