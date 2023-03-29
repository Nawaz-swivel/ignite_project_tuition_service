package com.swivel.ignite.tuition.service;

import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.entity.Tuition;
import com.swivel.ignite.tuition.exception.TuitionAlreadyExistsException;
import com.swivel.ignite.tuition.exception.TuitionNotFoundException;
import com.swivel.ignite.tuition.exception.TuitionServiceException;
import com.swivel.ignite.tuition.repository.TuitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Tuition Service
 */
@Service
public class TuitionService {

    private final StudentService studentService;
    private final TuitionRepository tuitionRepository;
    private final PaymentService paymentService;

    @Autowired
    public TuitionService(TuitionRepository tuitionRepository, StudentService studentService,
                          PaymentService paymentService) {
        this.studentService = studentService;
        this.tuitionRepository = tuitionRepository;
        this.paymentService = paymentService;
    }

    /**
     * This method creates a Tuition in the database
     *
     * @param tuition tuition
     */
    public void createTuition(Tuition tuition) {
        try {
            if (isTuitionExists(tuition.getName()))
                throw new TuitionAlreadyExistsException("Tuition already exists in DB");
            tuitionRepository.save(tuition);
        } catch (DataAccessException e) {
            throw new TuitionServiceException("Failed to save tuition to DB for tuition id: {}" + tuition.getId(), e);
        }
    }

    /**
     * This method finds a tuition by id
     *
     * @param id tuition id
     * @return Tuition/ null
     */
    public Tuition findById(String id) {
        try {
            Optional<Tuition> optionalTuition = tuitionRepository.findById(id);
            if (!optionalTuition.isPresent())
                throw new TuitionNotFoundException("Tuition not found for id: " + id);
            return optionalTuition.get();
        } catch (DataAccessException e) {
            throw new TuitionServiceException("Failed to get tuition from DB for tuition id: " + id, e);
        }
    }

    /**
     * This method deletes a tuition
     *
     * @param tuition tuition
     */
    @Transactional
    public void deleteTuition(Tuition tuition, String token) {
        try {
            Set<String> studentIds = tuition.getStudentIds();
            for (String s : studentIds) {
                studentService.removeTuition(s, tuition.getId(), token);
            }
            paymentService.deleteByTuitionId(tuition.getId(), token);
            tuitionRepository.delete(tuition);
        } catch (DataAccessException | IOException e) {
            throw new TuitionServiceException("Failed to delete tuition of id: " + tuition.getId(), e);
        }
    }

    /**
     * This method checks if tuition already exists in the DB
     *
     * @param name tuition name
     * @return true/false
     */
    private boolean isTuitionExists(String name) {
        try {
            return tuitionRepository.findByName(name).isPresent();
        } catch (DataAccessException e) {
            throw new TuitionServiceException("Failed to check for tuition existence in DB for name: " + name, e);
        }
    }

    /**
     * This method returns all tuition
     *
     * @return list of tuition
     */
    public List<Tuition> getAll() {
        try {
            return tuitionRepository.findAll();
        } catch (DataAccessException e) {
            throw new TuitionServiceException("Failed to to get all tuition", e);
        }
    }

    /**
     * This method is used to add a student to the tuition
     *
     * @param studentResponseDto student details
     * @param tuition            tuition
     * @return student response/ null
     */
    public StudentResponseDto addStudentToTuition(StudentResponseDto studentResponseDto, Tuition tuition, String token) {
        try {
            studentService.addTuition(studentResponseDto.getStudentId(), tuition.getId(), token);
            studentResponseDto.setTuitionId(tuition.getId());
            studentResponseDto.setTuitionJoinedOn(new Date());
            tuition.addStudentId(studentResponseDto.getStudentId());
            tuitionRepository.save(tuition);
            return studentResponseDto;
        } catch (DataAccessException | IOException e) {
            throw new TuitionServiceException("Failed to add student to tuition of id: " + tuition.getId(), e);
        }
    }

    /**
     * This method is used to remove a student from tuition
     *
     * @param studentResponseDto student details
     * @param tuition            tuition
     * @return student response/ null
     */
    public StudentResponseDto removeStudentFromTuition(StudentResponseDto studentResponseDto, Tuition tuition, String token) {
        try {
            studentService.removeTuition(studentResponseDto.getStudentId(), tuition.getId(), token);
            studentResponseDto.setTuitionId(null);
            studentResponseDto.setTuitionJoinedOn(null);
            tuition.removeStudentId(studentResponseDto.getStudentId());
            tuitionRepository.save(tuition);
            return studentResponseDto;
        } catch (DataAccessException | IOException e) {
            throw new TuitionServiceException("Failed to remove student to tuition of id: " + tuition.getId(), e);
        }
    }
}
