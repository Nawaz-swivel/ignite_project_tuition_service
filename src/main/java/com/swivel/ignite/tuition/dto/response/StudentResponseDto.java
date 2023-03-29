package com.swivel.ignite.tuition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Student DTO for response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto extends ResponseDto {

    private String studentId;
    private String name;
    private String tuitionId;
    private Date tuitionJoinedOn;
}
