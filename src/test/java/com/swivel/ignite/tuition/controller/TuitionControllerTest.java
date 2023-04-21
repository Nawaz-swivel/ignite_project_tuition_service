package com.swivel.ignite.tuition.controller;

import com.swivel.ignite.tuition.dto.request.TuitionCreateRequestDto;
import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.entity.Tuition;
import com.swivel.ignite.tuition.enums.ErrorResponseStatusType;
import com.swivel.ignite.tuition.enums.SuccessResponseStatusType;
import com.swivel.ignite.tuition.exception.*;
import com.swivel.ignite.tuition.service.StudentService;
import com.swivel.ignite.tuition.service.TuitionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class tests {@link TuitionController} class
 */
class TuitionControllerTest {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN = "Bearer 123456789";
    private static final String TUITION_ID = "tid-123456789";
    private static final String STUDENT_ID = "sid-123456789";
    private static final String STUDENT_NAME = "Mohamed Nawaz";
    private static final String TUITION_NAME = "Perera Tuition";
    private static final String TUITION_LOCATION = "Nittambuwa";
    private static final String SUCCESS_STATUS = "SUCCESS";
    private static final String ERROR_STATUS = "ERROR";
    private static final String SUCCESS_MESSAGE = "Successfully returned the data.";
    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";
    private static final String ERROR = "ERROR";
    private static final String CREATE_TUITION_URI = "/api/v1/tuition";
    private static final String GET_TUITION_BY_ID_URI = "/api/v1/tuition/get/{tuitionId}";
    private static final String DELETE_TUITION_ID_URI = "/api/v1/tuition/delete/{tuitionId}";
    private static final String GET_ALL_TUITION_URI = "/api/v1/tuition/get/all";
    private static final String ADD_STUDENT_TO_TUITION = "/api/v1/tuition/add/student/{studentId}/tuition/{tuitionId}";
    private static final String REMOVE_STUDENT_FROM_TUITION = "/api/v1/tuition/remove/student/{studentId}/tuition/{tuitionId}";
    private MockMvc mockMvc;
    @Mock
    private TuitionService tuitionService;
    @Mock
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        TuitionController tuitionController = new TuitionController(tuitionService, studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(tuitionController)
                .setControllerAdvice(new CustomizedExceptionHandling())
                .build();
    }

    /**
     * Start of tests for create tuition
     * Api context: /api/v1/tuition
     */
    @Test
    void Should_ReturnOk_When_CreatingTuitionIsSuccessful() throws Exception {
        doNothing().when(tuitionService).createTuition(any(Tuition.class));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_TUITION_URI)
                        .content(getSampleTuitionCreateRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.CREATE_TUITION.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.CREATE_TUITION.getCode()))
                .andExpect(jsonPath("$.data.name").value(TUITION_NAME))
                .andExpect(jsonPath("$.data.location").value(TUITION_LOCATION))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingTuitionForMissingRequiredFields() throws Exception {
        TuitionCreateRequestDto dto = getSampleTuitionCreateRequestDto();
        dto.setLocation("");

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_TUITION_URI)
                        .content(dto.toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_CreatingTuitionForTuitionAlreadyExists() throws Exception {
        doThrow(new TuitionAlreadyExistsException(ERROR)).when(tuitionService).createTuition(any(Tuition.class));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_TUITION_URI)
                        .content(getSampleTuitionCreateRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.TUITION_ALREADY_EXISTS
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.TUITION_ALREADY_EXISTS
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_CreatingTuitionIsFailed() throws Exception {
        doThrow(new TuitionServiceException(ERROR)).when(tuitionService).createTuition(any(Tuition.class));

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_TUITION_URI)
                        .content(getSampleTuitionCreateRequestDto().toJson())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * Start of tests for get tuition by id
     * Api context: /api/v1/tuition/get/{tuitionId}
     */
    @Test
    void Should_ReturnOk_When_GettingTuitionByIdIsSuccessful() throws Exception {
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());

        String uri = GET_TUITION_BY_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.READ_TUITION.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.READ_TUITION.getCode()))
                .andExpect(jsonPath("$.data.tuitionId").value(TUITION_ID))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_GettingTuitionByIdForTuitionNotFound() throws Exception {
        when(tuitionService.findById(anyString())).thenThrow(new TuitionNotFoundException(ERROR));

        String uri = GET_TUITION_BY_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.TUITION_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.TUITION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_GettingTuitionByIdIsFailed() throws Exception {
        when(tuitionService.findById(anyString())).thenThrow(new TuitionServiceException(ERROR));

        String uri = GET_TUITION_BY_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * Start of tests for delete tuition
     * Api context: /api/v1/tuition/delete/{tuitionId}
     */
    @Test
    void Should_ReturnOk_When_DeletingTuitionIsSuccessful() throws Exception {
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());
        doNothing().when(tuitionService).deleteTuition(any(Tuition.class), anyString());

        String uri = DELETE_TUITION_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(202))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.DELETE_TUITION.getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.DELETE_TUITION.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_DeletingTuitionForTuitionNotFound() throws Exception {
        when(tuitionService.findById(anyString())).thenThrow(new TuitionNotFoundException(ERROR));

        String uri = DELETE_TUITION_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.TUITION_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.TUITION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_DeletingTuitionForFailedToRemoveTuitionFromStudent() throws Exception {
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());
        doThrow(new StudentServiceHttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR))
                .when(tuitionService).deleteTuition(any(Tuition.class), anyString());

        String uri = DELETE_TUITION_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_DeletingTuitionForFailedToDeleteAllPaymentByTuitionId() throws Exception {
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());
        doThrow(new PaymentServiceHttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR))
                .when(tuitionService).deleteTuition(any(Tuition.class), anyString());

        String uri = DELETE_TUITION_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.PAYMENT_INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.PAYMENT_INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_DeletingTuitionIsFailed() throws Exception {
        when(tuitionService.findById(anyString())).thenThrow(new TuitionServiceException(ERROR));

        String uri = DELETE_TUITION_ID_URI.replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.delete(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * Start of tests for get all tuition
     * Api context: /api/v1/tuition/get/all
     */
    @Test
    void Should_ReturnOk_When_GettingAllTuitionIsSuccessful() throws Exception {
        when(tuitionService.getAll()).thenReturn(getSampleTuitionList());

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_TUITION_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.RETURNED_ALL_TUITION
                        .getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.RETURNED_ALL_TUITION
                        .getCode()))
                .andExpect(jsonPath("$.data.tuitionList[0].tuitionId").value(TUITION_ID))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_GettingAllTuitionIsFailed() throws Exception {
        when(tuitionService.getAll()).thenThrow(new TuitionServiceException(ERROR));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_ALL_TUITION_URI)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * Start of tests for add student to tuition
     * Api context: /api/v1/tuition/add/student/{studentId}/tuition/{tuitionId}
     */
    @Test
    void Should_ReturnOk_When_AddingStudentToTuitionIsSuccessful() throws Exception {
        when(studentService.findById(anyString(), anyString())).thenReturn(getSampleStudentResponseDto());
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());
        when(tuitionService.addStudentToTuition(any(StudentResponseDto.class), any(Tuition.class), anyString()))
                .thenReturn(getSampleStudentResponseDto());

        String uri = ADD_STUDENT_TO_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.ADD_TUITION_STUDENT
                        .getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.ADD_TUITION_STUDENT
                        .getCode()))
                .andExpect(jsonPath("$.data.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_AddingStudentToTuitionForStudentAlreadyEnrolledInATuition() throws Exception {
        StudentResponseDto studentResponseDto = getSampleStudentResponseDto();
        studentResponseDto.setTuitionId(TUITION_ID);

        when(studentService.findById(anyString(), anyString())).thenReturn(studentResponseDto);

        String uri = ADD_STUDENT_TO_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType
                        .STUDENT_ALREADY_ENROLLED_IN_A_TUITION.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType
                        .STUDENT_ALREADY_ENROLLED_IN_A_TUITION.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_AddingStudentToTuitionForTuitionNotFound() throws Exception {
        when(studentService.findById(anyString(), anyString())).thenReturn(getSampleStudentResponseDto());
        when(tuitionService.findById(anyString())).thenThrow(new TuitionNotFoundException(ERROR));

        String uri = ADD_STUDENT_TO_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.TUITION_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.TUITION_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_AddingStudentToTuitionForFailedToGetStudentInfo() throws Exception {
        when(studentService.findById(anyString(), anyString()))
                .thenThrow(new StudentServiceHttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR));

        String uri = ADD_STUDENT_TO_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_AddingStudentToTuitionIsFailed() throws Exception {
        when(studentService.findById(anyString(), anyString())).thenReturn(getSampleStudentResponseDto());
        when(tuitionService.findById(anyString())).thenThrow(new TuitionServiceException(ERROR));

        String uri = ADD_STUDENT_TO_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * Start of tests for remove student from tuition
     * Api context: /api/v1/tuition/remove/student/{studentId}/tuition/{tuitionId}
     */
    @Test
    void Should_ReturnOk_When_RemovingStudentFromTuitionIsSuccessful() throws Exception {
        StudentResponseDto studentResponseDto = getSampleStudentResponseDto();
        studentResponseDto.setTuitionId(TUITION_ID);

        when(studentService.findById(anyString(), anyString())).thenReturn(studentResponseDto);
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());
        when(tuitionService.removeStudentFromTuition(any(StudentResponseDto.class), any(Tuition.class), anyString()))
                .thenReturn(getSampleStudentResponseDto());

        String uri = REMOVE_STUDENT_FROM_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(SUCCESS_STATUS))
                .andExpect(jsonPath("$.message").value(SuccessResponseStatusType.REMOVE_TUITION_STUDENT
                        .getMessage()))
                .andExpect(jsonPath("$.statusCode").value(SuccessResponseStatusType.REMOVE_TUITION_STUDENT
                        .getCode()))
                .andExpect(jsonPath("$.data.name").value(STUDENT_NAME))
                .andExpect(jsonPath("$.displayMessage").value(SUCCESS_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_RemovingStudentFromTuitionForStudentNotEnrolledInTuition() throws Exception {
        when(studentService.findById(anyString(), anyString())).thenReturn(getSampleStudentResponseDto());
        when(tuitionService.findById(anyString())).thenReturn(getSampleTuition());

        String uri = REMOVE_STUDENT_FROM_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.STUDENT_NOT_ENROLLED_IN_TUITION
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.STUDENT_NOT_ENROLLED_IN_TUITION
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnBadRequest_When_RemovingStudentFromTuitionForTuitionNotFound() throws Exception {
        StudentResponseDto studentResponseDto = getSampleStudentResponseDto();
        studentResponseDto.setTuitionId(TUITION_ID);

        when(studentService.findById(anyString(), anyString())).thenReturn(studentResponseDto);
        when(tuitionService.findById(anyString())).thenThrow(new TuitionNotFoundException(ERROR));

        String uri = REMOVE_STUDENT_FROM_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.TUITION_NOT_FOUND
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.TUITION_NOT_FOUND
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_RemovingStudentFromTuitionForFailedToGetStudentInfo() throws Exception {
        when(studentService.findById(anyString(), anyString()))
                .thenThrow(new StudentServiceHttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR));

        String uri = REMOVE_STUDENT_FROM_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .header(AUTH_HEADER, TOKEN)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.STUDENT_INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    @Test
    void Should_ReturnInternalServerError_When_RemovingStudentFromTuitionIsFailed() throws Exception {
        StudentResponseDto studentResponseDto = getSampleStudentResponseDto();
        studentResponseDto.setTuitionId(TUITION_ID);

        when(studentService.findById(anyString(), anyString())).thenReturn(studentResponseDto);
        when(tuitionService.findById(anyString())).thenThrow(new TuitionServiceException(ERROR));

        String uri = REMOVE_STUDENT_FROM_TUITION.replace("{studentId}", STUDENT_ID)
                .replace("{tuitionId}", TUITION_ID);
        mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.status").value(ERROR_STATUS))
                .andExpect(jsonPath("$.message").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getMessage()))
                .andExpect(jsonPath("$.errorCode").value(ErrorResponseStatusType.INTERNAL_SERVER_ERROR
                        .getCode()))
                .andExpect(jsonPath("$.displayMessage").value(ERROR_MESSAGE));
    }

    /**
     * This method returns a sample TuitionCreateRequestDto
     *
     * @return TuitionCreateRequestDto
     */
    private TuitionCreateRequestDto getSampleTuitionCreateRequestDto() {
        TuitionCreateRequestDto requestDto = new TuitionCreateRequestDto();
        requestDto.setName(TUITION_NAME);
        requestDto.setLocation(TUITION_LOCATION);
        return requestDto;
    }

    /**
     * This method returns a sample tuition
     *
     * @return Tuition
     */
    private Tuition getSampleTuition() {
        Tuition tuition = new Tuition();
        tuition.setId(TUITION_ID);
        return tuition;
    }

    /**
     * This method returns a sample tuition list
     *
     * @return Tuition List
     */
    private List<Tuition> getSampleTuitionList() {
        List<Tuition> tuitionList = new ArrayList<>();
        tuitionList.add(getSampleTuition());
        return tuitionList;
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
