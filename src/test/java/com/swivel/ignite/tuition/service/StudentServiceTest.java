package com.swivel.ignite.tuition.service;

import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.exception.StudentServiceHttpClientErrorException;
import com.swivel.ignite.tuition.wrapper.StudentResponseWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link StudentService} class
 */
class StudentServiceTest {

    private static final String TOKEN = "Bearer 123456789";
    private static final String STUDENT_ID = "sid-123456789";
    private static final String TUITION_ID = "tid-123456789";
    private static final String BASE_URL = "http://localhost:8080/ignite-student-service";
    private static final String FIND_BY_ID_URL = "/api/v1/student/get/{studentId}";
    private static final String ADD_TUITION_TO_STUDENT_URL = "/api/v1/student/add/student/{studentId}/tuition/{tuitionId}";
    private static final String REMOVE_TUITION_FROM_STUDENT_URL = "/api/v1/student/remove/student/{studentId}/tuition/{tuitionId}";
    private StudentService studentService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        initMocks(this);
        studentService = new StudentService(BASE_URL, FIND_BY_ID_URL, ADD_TUITION_TO_STUDENT_URL,
                REMOVE_TUITION_FROM_STUDENT_URL, restTemplate);
    }

    /**
     * Start of tests for findById method
     */
    @Test
    void Should_ReturnStudentResponseDto_When_FindingByIdIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenReturn(getSampleStudentResponseEntity());
        assertEquals(STUDENT_ID, studentService.findById(STUDENT_ID, TOKEN).getStudentId());
    }

    @Test
    void Should_ThrowStudentServiceHttpClientErrorException_When_FindingByStudentIdIsFailed() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        StudentServiceHttpClientErrorException exception =
                assertThrows(StudentServiceHttpClientErrorException.class, () -> studentService
                        .findById(STUDENT_ID, TOKEN));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to get student by studentId",
                exception.getMessage());
    }

    /**
     * Start of tests for addTuition method
     */
    @Test
    void Should_AddTuitionToStudent_When_AddingTuitionIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenReturn(getSampleResponseEntity());
        studentService.addTuition(STUDENT_ID, TUITION_ID, TOKEN);
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class),
                any(HttpEntity.class), any(Class.class), anyMap());
    }

    @Test
    void Should_ThrowStudentServiceHttpClientErrorException_When_AddingTuitionIsFailed() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        StudentServiceHttpClientErrorException exception =
                assertThrows(StudentServiceHttpClientErrorException.class, () -> studentService
                        .addTuition(STUDENT_ID, TUITION_ID, TOKEN));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to add tuition to student",
                exception.getMessage());
    }

    /**
     * Start of tests for removeTuition method
     */
    @Test
    void Should_RemoveTuitionFromStudent_When_RemovingTuitionIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenReturn(getSampleResponseEntity());
        studentService.removeTuition(STUDENT_ID, TUITION_ID, TOKEN);
        verify(restTemplate, times(1)).exchange(anyString(), any(HttpMethod.class),
                any(HttpEntity.class), any(Class.class), anyMap());
    }

    @Test
    void Should_ThrowStudentServiceHttpClientErrorException_When_RemovingTuitionIsFailed() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        StudentServiceHttpClientErrorException exception =
                assertThrows(StudentServiceHttpClientErrorException.class, () -> studentService
                        .removeTuition(STUDENT_ID, TUITION_ID, TOKEN));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to remove tuition from student",
                exception.getMessage());
    }

    /**
     * This method returns a sample ResponseEntity
     *
     * @return ResponseEntity
     */
    private ResponseEntity<String> getSampleResponseEntity() {
        return new ResponseEntity<>("Response", HttpStatus.OK);
    }

    /**
     * This method returns a sample StudentResponseEntity
     *
     * @return ResponseEntity
     */
    private ResponseEntity<StudentResponseWrapper> getSampleStudentResponseEntity() {
        return new ResponseEntity<>(getSampleStudentResponseWrapper(), HttpStatus.OK);
    }

    /**
     * This method returns a sample StudentResponseWrapper
     *
     * @return StudentResponseWrapper
     */
    private StudentResponseWrapper getSampleStudentResponseWrapper() {
        StudentResponseWrapper responseWrapper = new StudentResponseWrapper();
        responseWrapper.setData(getSampleStudentResponseDto());
        return responseWrapper;
    }

    /**
     * This method returns a sample StudentResponseDto
     *
     * @return StudentResponseDto
     */
    private StudentResponseDto getSampleStudentResponseDto() {
        StudentResponseDto responseDto = new StudentResponseDto();
        responseDto.setStudentId(STUDENT_ID);
        return responseDto;
    }
}
