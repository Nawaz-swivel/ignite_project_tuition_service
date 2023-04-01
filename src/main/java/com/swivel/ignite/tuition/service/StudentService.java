package com.swivel.ignite.tuition.service;

import com.swivel.ignite.tuition.dto.response.StudentResponseDto;
import com.swivel.ignite.tuition.exception.StudentServiceHttpClientErrorException;
import com.swivel.ignite.tuition.wrapper.StudentResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Student Microservice
 */
@Slf4j
@Service
public class StudentService {

    private static final String AUTH_HEADER = "Authorization";
    private static final String FAILED_TO_GET_STUDENT_BY_ID = "Failed to get student by studentId";
    private static final String FAILED_TO_ADD_TUITION_TO_STUDENT = "Failed to add tuition to student";
    private static final String FAILED_TO_REMOVE_TUITION_FROM_STUDENT = "Failed to remove tuition from student";
    private static final String STUDENT_ID = "studentId";
    private static final String TUITION_ID = "tuitionId";
    private final RestTemplate restTemplate;
    private final String getFindByIdUrl;
    private final String getAddTuitionToStudentUrl;
    private final String getRemoveTuitionFromStudentUrl;

    public StudentService(@Value("${student.baseUrl}") String baseUrl,
                          @Value("${student.findByIdUrl}") String findByIdUrl,
                          @Value("${student.addTuitionToStudentUrl}") String addTuitionToStudentUrl,
                          @Value("${student.removeTuitionFromStudentUrl}") String removeTuitionFromStudentUrl,
                          RestTemplate restTemplate) {
        this.getFindByIdUrl = baseUrl + findByIdUrl;
        this.getAddTuitionToStudentUrl = baseUrl + addTuitionToStudentUrl;
        this.getRemoveTuitionFromStudentUrl = baseUrl + removeTuitionFromStudentUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * This method is used to find a student by id in student microservice
     *
     * @param studentId student id
     * @return success(student)/error response
     * @throws IOException
     */
    public StudentResponseDto findById(String studentId, String token) throws IOException {
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put(STUDENT_ID, studentId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(getFindByIdUrl).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_HEADER, token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling student microservice to get the student by studentId. url: {}", getFindByIdUrl);
            ResponseEntity<StudentResponseWrapper> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
                    entity, StudentResponseWrapper.class, uriParam);
            String responseBody = Objects.requireNonNull(result.getBody()).getData().toLogJson();
            log.debug("Getting the student by studentId was successful. statusCode: {}, response: {}",
                    result.getStatusCode(), responseBody);
            return Objects.requireNonNull(result.getBody()).getData();
        } catch (HttpClientErrorException e) {
            throw new StudentServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_GET_STUDENT_BY_ID,
                    e.getResponseBodyAsString(), e);
        }
    }

    /**
     * This method is used to add a tuition to student in student microservice
     *
     * @param studentId student id
     * @param tuitionId tuition id
     * @return success/ error response
     * @throws IOException
     */
    public void addTuition(String studentId, String tuitionId, String token) throws IOException {
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put(STUDENT_ID, studentId);
        uriParam.put(TUITION_ID, tuitionId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(getAddTuitionToStudentUrl).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_HEADER, token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling student service to add the tuition to student. url: {}", getAddTuitionToStudentUrl);
            ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
                    String.class, uriParam);
            log.debug("Adding tuition to student was successful. statusCode: {}", result.getStatusCode());
        } catch (HttpClientErrorException e) {
            throw new StudentServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_ADD_TUITION_TO_STUDENT,
                    e.getResponseBodyAsString(), e);
        }
    }

    /**
     * This method is used to remove a tuition from student in student microservice
     *
     * @param studentId student id
     * @param tuitionId tuition id
     * @return success/ error response
     * @throws IOException
     */
    public void removeTuition(String studentId, String tuitionId, String token) throws IOException {
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put(STUDENT_ID, studentId);
        uriParam.put(TUITION_ID, tuitionId);
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(getRemoveTuitionFromStudentUrl).build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_HEADER, token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            log.debug("Calling student service to remove the tuition from student. url: {}", getRemoveTuitionFromStudentUrl);
            ResponseEntity<String> result = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
                    String.class, uriParam);
            log.debug("Removing tuition from student was successful. statusCode: {}", result.getStatusCode());
        } catch (HttpClientErrorException e) {
            throw new StudentServiceHttpClientErrorException(e.getStatusCode(), FAILED_TO_REMOVE_TUITION_FROM_STUDENT,
                    e.getResponseBodyAsString(), e);
        }
    }
}
