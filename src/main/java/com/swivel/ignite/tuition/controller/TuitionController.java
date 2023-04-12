package com.swivel.ignite.tuition.controller;

import com.swivel.ignite.tuition.dto.request.TuitionCreateRequestDto;
import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.dto.response.TuitionListResponseDto;
import com.swivel.ignite.tuition.dto.response.TuitionResponseDto;
import com.swivel.ignite.tuition.entity.Tuition;
import com.swivel.ignite.tuition.enums.ErrorResponseStatusType;
import com.swivel.ignite.tuition.enums.SuccessResponseStatusType;
import com.swivel.ignite.tuition.service.StudentService;
import com.swivel.ignite.tuition.service.TuitionService;
import com.swivel.ignite.tuition.wrapper.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Tuition Controller
 */
@RestController
@RequestMapping("api/v1/tuition")
@Slf4j
public class TuitionController extends Controller {

    private final TuitionService tuitionService;
    private final StudentService studentService;

    @Autowired
    public TuitionController(TuitionService tuitionService, StudentService studentService) {
        this.tuitionService = tuitionService;
        this.studentService = studentService;
    }

    /**
     * This method creates a new tuition class
     *
     * @param requestDto tuition create request dto
     * @return success(tuition response)/ error response
     */
    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> createTuition(@RequestBody TuitionCreateRequestDto requestDto) {
        if (!requestDto.isRequiredAvailable()) {
            log.error("Required fields missing in tuition create request DTO for creating tuition");
            return getBadRequestResponse(ErrorResponseStatusType.MISSING_REQUIRED_FIELDS);
        }
        Tuition tuition = new Tuition(requestDto);
        tuitionService.createTuition(tuition);
        TuitionResponseDto responseDto = new TuitionResponseDto(tuition);
        log.debug("Created tuition {}", responseDto.toLogJson());
        return getSuccessResponse(SuccessResponseStatusType.CREATE_TUITION, responseDto);
    }

    /**
     * This method returns a tuition class by id
     *
     * @param id tuition class id
     * @return success(tuition response)/ error response
     */
    @GetMapping(path = "/get/{tuitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getTuitionById(@PathVariable(name = "tuitionId") String id) {
        Tuition tuition = tuitionService.findById(id);
        TuitionResponseDto responseDto = new TuitionResponseDto(tuition);
        log.debug("Successfully returned the tuition {}", responseDto.toLogJson());
        return getSuccessResponse(SuccessResponseStatusType.READ_TUITION, responseDto);
    }

    /**
     * This method deletes a tuition class by id
     *
     * @param tuitionId tuitionId
     * @return success/ error response
     */
    @DeleteMapping(path = "/delete/{tuitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> deleteTuition(@PathVariable(name = "tuitionId") String tuitionId,
                                                         HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        Tuition tuition = tuitionService.findById(tuitionId);
        tuitionService.deleteTuition(tuition, token);
        log.debug("Deleted tuition of id: {}", tuitionId);
        return getSuccessResponse(SuccessResponseStatusType.DELETE_TUITION, null);
    }

    /**
     * This method is used to get all tuition
     *
     * @return success(tuition list)/ error response
     */
    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> getAllTuition() {
        List<Tuition> tuitionList = tuitionService.getAll();
        TuitionListResponseDto responseDto = new TuitionListResponseDto(tuitionList);
        log.debug("Returned all tuition");
        return getSuccessResponse(SuccessResponseStatusType.RETURNED_ALL_TUITION, responseDto);
    }

    /**
     * This method adds a student to the tuition
     *
     * @param studentId student id
     * @param tuitionId tuition id
     * @return success/ error response
     */
    @PostMapping(path = "/add/student/{studentId}/tuition/{tuitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> addStudentToTuition(@PathVariable(name = "studentId") String studentId,
                                                               @PathVariable(name = "tuitionId") String tuitionId,
                                                               HttpServletRequest request) throws IOException {
        String token = request.getHeader(AUTH_HEADER);
        StudentResponseDto studentResponseDto = studentService.findById(studentId, token);
        if (studentResponseDto.getTuitionId() != null) {
            log.error("Student already enrolled in a tuition");
            return getBadRequestResponse(ErrorResponseStatusType.STUDENT_ALREADY_ENROLLED_IN_A_TUITION);
        }
        Tuition tuition = tuitionService.findById(tuitionId);
        StudentResponseDto responseDto = tuitionService.addStudentToTuition(studentResponseDto, tuition, token);
        log.debug("Successfully added student of id: {} to the tuition", studentId);
        return getSuccessResponse(SuccessResponseStatusType.ADD_TUITION_STUDENT, responseDto);
    }

    /**
     * This method removes a student from the tuition
     *
     * @param studentId student id
     * @param tuitionId tuition id
     * @return success/ error response
     */
    @PostMapping(path = "/remove/student/{studentId}/tuition/{tuitionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseWrapper> removeStudentFromTuition(@PathVariable(name = "studentId") String studentId,
                                                                    @PathVariable(name = "tuitionId") String tuitionId,
                                                                    HttpServletRequest request) throws IOException {
        String token = request.getHeader(AUTH_HEADER);
        StudentResponseDto studentResponseDto = studentService.findById(studentId, token);
        Tuition tuition = tuitionService.findById(tuitionId);
        if (studentResponseDto.getTuitionId() == null || !studentResponseDto.getTuitionId().equals(tuition.getId())) {
            log.error("Student is not enrolled in tuition id: " + tuition.getId());
            return getBadRequestResponse(ErrorResponseStatusType.STUDENT_NOT_ENROLLED_IN_TUITION);
        }
        StudentResponseDto responseDto = tuitionService.removeStudentFromTuition(studentResponseDto, tuition, token);
        log.debug("Successfully removed student of id: {} from the tuition", studentId);
        return getSuccessResponse(SuccessResponseStatusType.REMOVE_TUITION_STUDENT, responseDto);
    }
}
