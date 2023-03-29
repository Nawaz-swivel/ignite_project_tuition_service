package com.swivel.ignite.tuition.entity;

import com.swivel.ignite.tuition.dto.request.TuitionCreateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * Tuition entity
 */
@Entity
@Table(name = "tuition")
@NoArgsConstructor
@Getter
@Setter
public class Tuition implements Serializable {

    @Transient
    private static final String TUITION_ID_PREFIX = "tid-";

    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String location;
    @ElementCollection
    private Set<String> studentIds;

    public Tuition(TuitionCreateRequestDto requestDto) {
        this.id = TUITION_ID_PREFIX + UUID.randomUUID();
        this.name = requestDto.getName();
        this.location = requestDto.getLocation();
    }

    public void addStudentId(String studentId) {
        this.studentIds.add(studentId);
    }

    public void removeStudentId(String studentId) {
        this.studentIds.remove(studentId);
    }
}
