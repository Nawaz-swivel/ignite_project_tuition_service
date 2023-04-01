package com.swivel.ignite.tuition.service;

import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.entity.Tuition;
import com.swivel.ignite.tuition.exception.TuitionAlreadyExistsException;
import com.swivel.ignite.tuition.exception.TuitionNotFoundException;
import com.swivel.ignite.tuition.exception.TuitionServiceException;
import com.swivel.ignite.tuition.repository.TuitionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link TuitionService} class
 */
class TuitionServiceTest {

    private static final String TOKEN = "Bearer 123456789";
    private static final String TUITION_ID = "tid-123456789";
    private static final String STUDENT_ID = "sid-123456789";
    private static final String TUITION_NAME = "Perera Tuition";
    private static final String STUDENT_NAME = "Mohamed Nawaz";
    private static final String ERROR = "ERROR";
    private TuitionService tuitionService;
    @Mock
    private StudentService studentService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private TuitionRepository tuitionRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
        tuitionService = new TuitionService(tuitionRepository, studentService, paymentService);
    }

    /**
     * Start of test for createTuition method
     */
    @Test
    void Should_CreateTuition_When_CreatingTuitionIsSuccessful() {
        when(tuitionRepository.findByName(anyString())).thenReturn(Optional.empty());
        tuitionService.createTuition(getSampleTuition());
        verify(tuitionRepository).save(any(Tuition.class));
    }

    @Test
    void Should_ThrowRegistrationServiceException_When_CreatingTuitionForFailedToCheckForTuitionInDB() {
        Tuition tuition = getSampleTuition();

        when(tuitionRepository.findByName(anyString())).thenThrow(new DataAccessException(ERROR) {
        });
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () ->
                tuitionService.createTuition(tuition));
        assertEquals("Failed to check for tuition existence in DB for name: " + TUITION_NAME, exception.getMessage());
    }

    @Test
    void Should_ThrowTuitionAlreadyExistsException_When_CreatingTuitionForTuitionAlreadyExists() {
        Tuition tuition = getSampleTuition();

        when(tuitionRepository.findByName(anyString())).thenReturn(Optional.of(getSampleTuition()));
        TuitionAlreadyExistsException exception = assertThrows(TuitionAlreadyExistsException.class, () ->
                tuitionService.createTuition(tuition));
        assertEquals("Tuition already exists in DB", exception.getMessage());
    }

    @Test
    void Should_ThrowRegistrationServiceException_When_CreatingTuitionIsFailed() {
        Tuition tuition = getSampleTuition();

        when(tuitionRepository.findByName(anyString())).thenReturn(Optional.empty());
        doThrow(new DataAccessException(ERROR) {
        }).when(tuitionRepository).save(any(Tuition.class));
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () ->
                tuitionService.createTuition(tuition));
        assertEquals("Failed to save tuition to DB for tuition id: {}" + TUITION_ID, exception.getMessage());
    }

    /**
     * Start of test for findById method
     */
    @Test
    void Should_ReturnTuition_When_FindingTuitionByIdIsSuccessful() {
        when(tuitionRepository.findById(anyString())).thenReturn(Optional.of(getSampleTuition()));
        assertEquals(TUITION_ID, tuitionService.findById(TUITION_ID).getId());
    }

    @Test
    void Should_ThrowTuitionNotFoundException_When_FindingTuitionByIdForTuitionNotFound() {
        when(tuitionRepository.findById(anyString())).thenReturn(Optional.empty());
        TuitionNotFoundException exception = assertThrows(TuitionNotFoundException.class, () ->
                tuitionService.findById(TUITION_ID));
        assertEquals("Tuition not found for id: " + TUITION_ID, exception.getMessage());
    }

    @Test
    void Should_ThrowRegistrationServiceException_When_FindingTuitionByIdIsFailed() {
        when(tuitionRepository.findById(anyString())).thenThrow(new DataAccessException(ERROR) {
        });
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () ->
                tuitionService.findById(TUITION_ID));
        assertEquals("Failed to get tuition from DB for tuition id: " + TUITION_ID, exception.getMessage());
    }

    /**
     * Start of test for deleteTuition method
     */
    @Test
    void Should_DeleteTuition_When_DeletingTuitionIsSuccessful() throws IOException {
        doNothing().when(studentService).removeTuition(anyString(), anyString(), anyString());
        doNothing().when(paymentService).deleteByTuitionId(anyString(), anyString());
        tuitionService.deleteTuition(getSampleTuition(), TOKEN);
        verify(tuitionRepository).delete(any(Tuition.class));
    }

    @Test
    void Should_ThrowRegistrationServiceException_When_DeletingTuitionIsFailed() throws IOException {
        Tuition tuition = getSampleTuition();

        doNothing().when(studentService).removeTuition(anyString(), anyString(), anyString());
        doNothing().when(paymentService).deleteByTuitionId(anyString(), anyString());
        doThrow(new DataAccessException(ERROR) {
        }).when(tuitionRepository).delete(any(Tuition.class));
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () ->
                tuitionService.deleteTuition(tuition, TOKEN));
        assertEquals("Failed to delete tuition of id: " + TUITION_ID, exception.getMessage());
    }

    /**
     * Start of test for getAll method
     */
    @Test
    void Should_ReturnAllTuition_When_GettingAllIsSuccessful() {
        when(tuitionRepository.findAll()).thenReturn(getSampleTuitionList());
        assertEquals(TUITION_ID, tuitionService.getAll().get(0).getId());
    }

    @Test
    void Should_ThrowRegistrationServiceException_When_GettingAllIsFailed() {
        when(tuitionRepository.findAll()).thenThrow(new DataAccessException(ERROR) {
        });
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () ->
                tuitionService.getAll());
        assertEquals("Failed to to get all tuition", exception.getMessage());
    }

    /**
     * Start of test for addStudentToTuition method
     */
    @Test
    void Should_ReturnStudentResponseDto_When_AddingStudentToTuitionIsSuccessful() throws IOException {
        doNothing().when(studentService).addTuition(anyString(), anyString(), anyString());
        assertEquals(STUDENT_NAME, tuitionService.addStudentToTuition(getSampleStudentResponseDto(),
                getSampleTuition(), TOKEN).getName());
        assertEquals(TUITION_ID, tuitionService.addStudentToTuition(getSampleStudentResponseDto(),
                getSampleTuition(), TOKEN).getTuitionId());
        verify(tuitionRepository, times(2)).save(any(Tuition.class));
    }

    @Test
    void Should_ThrowTuitionServiceException_When_AddingStudentToTuitionIsFailed() throws IOException {
        StudentResponseDto studentResponseDto = getSampleStudentResponseDto();
        Tuition tuition = getSampleTuition();

        doNothing().when(studentService).addTuition(anyString(), anyString(), anyString());
        when(tuitionRepository.save(any(Tuition.class))).thenThrow(new DataAccessException(ERROR) {
        });
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () -> tuitionService
                .addStudentToTuition(studentResponseDto, tuition, TOKEN));
        assertEquals("Failed to add student to tuition of id: " + TUITION_ID, exception.getMessage());
    }

    /**
     * Start of test for removeStudentFromTuition method
     */
    @Test
    void Should_ReturnStudentResponseDto_When_RemovingStudentFromTuitionIsSuccessful() throws IOException {
        doNothing().when(studentService).removeTuition(anyString(), anyString(), anyString());
        assertEquals(STUDENT_NAME, tuitionService.removeStudentFromTuition(getSampleStudentResponseDto(),
                getSampleTuition(), TOKEN).getName());
        assertNull(tuitionService.removeStudentFromTuition(getSampleStudentResponseDto(),
                getSampleTuition(), TOKEN).getTuitionId());
        verify(tuitionRepository, times(2)).save(any(Tuition.class));
    }

    @Test
    void Should_ThrowTuitionServiceException_When_RemovingStudentFromTuitionIsFailed() throws IOException {
        StudentResponseDto studentResponseDto = getSampleStudentResponseDto();
        Tuition tuition = getSampleTuition();

        doNothing().when(studentService).removeTuition(anyString(), anyString(), anyString());
        when(tuitionRepository.save(any(Tuition.class))).thenThrow(new DataAccessException(ERROR) {
        });
        TuitionServiceException exception = assertThrows(TuitionServiceException.class, () -> tuitionService
                .removeStudentFromTuition(studentResponseDto, tuition, TOKEN));
        assertEquals("Failed to remove student to tuition of id: " + TUITION_ID, exception.getMessage());
    }

    /**
     * This method returns a sample Tuition
     *
     * @return Tuition
     */
    private Tuition getSampleTuition() {
        Tuition tuition = new Tuition();
        tuition.setId(TUITION_ID);
        tuition.setName(TUITION_NAME);
        tuition.setStudentIds(getSampleStudentIds());
        return tuition;
    }

    /**
     * This method returns a sample Tuition list
     *
     * @return Tuition List
     */
    private List<Tuition> getSampleTuitionList() {
        List<Tuition> tuitionList = new ArrayList<>();
        tuitionList.add(getSampleTuition());
        return tuitionList;
    }

    /**
     * This method returns a sample Student Set
     *
     * @return Student Set
     */
    private Set<String> getSampleStudentIds() {
        Set<String> studentIds = new HashSet<>();
        studentIds.add(STUDENT_ID);
        return studentIds;
    }

    /**
     * This method returns a sample StudentResponseDto
     *
     * @return StudentResponseDto
     */
    private StudentResponseDto getSampleStudentResponseDto() {
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setName(STUDENT_NAME);
        return responseDto;
    }
}
