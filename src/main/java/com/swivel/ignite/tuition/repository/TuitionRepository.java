package com.swivel.ignite.tuition.repository;

import com.swivel.ignite.tuition.entity.Tuition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Tuition Repository
 */
@Repository
public interface TuitionRepository extends JpaRepository<Tuition, String> {

    /**
     * This method finds a Tuition by name
     *
     * @param name tuition name
     * @return Tuition/ null
     */
    Optional<Tuition> findByName(String name);
}
