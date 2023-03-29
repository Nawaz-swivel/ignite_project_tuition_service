package com.swivel.ignite.tuition.dto.response;

import com.swivel.ignite.tuition.entity.Tuition;
import lombok.Getter;

import java.util.Set;

/**
 * Tuition DTO for response
 */
@Getter
public class TuitionResponseDto extends ResponseDto {

    private final String tuitionId;
    private final String name;
    private final String location;
    private final Set<String> studentIds;

    public TuitionResponseDto(Tuition tuition) {
        this.tuitionId = tuition.getId();
        this.name = tuition.getName();
        this.location = tuition.getLocation();
        this.studentIds = tuition.getStudentIds();
    }
}
