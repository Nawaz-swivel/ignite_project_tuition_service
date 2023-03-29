package com.swivel.ignite.tuition.wrapper;

import com.swivel.ignite.tuition.dto.BaseDto;
import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.enums.ResponseStatusType;
import lombok.Getter;
import lombok.Setter;

/**
 * Student response wrapper
 */
@Getter
@Setter
public class StudentResponseWrapper implements BaseDto {

    private ResponseStatusType status;
    private String message;
    private StudentResponseDto data;
    private String displayMessage;

    @Override
    public String toLogJson() {
        return toJson();
    }
}
