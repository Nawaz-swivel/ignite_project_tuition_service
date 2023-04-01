package com.swivel.ignite.tuition.service;

import com.swivel.ignite.tuition.exception.PaymentServiceHttpClientErrorException;
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
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class tests {@link PaymentService} class
 */
class PaymentServiceTest {

    private static final String TUITION_ID = "tid-123456789";
    private static final String TOKEN = "Bearer 123456789";
    private static final String BASE_URL = "http://localhost:8083/ignite-payment-service";
    private static final String DELETE_BY_TUITION_ID_URL = "/api/v1/payment/delete/all/tuition/{tuitionId}";
    private PaymentService paymentService;
    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        initMocks(this);
        paymentService = new PaymentService(BASE_URL, DELETE_BY_TUITION_ID_URL, restTemplate);
    }

    /**
     * Start of tests for deleteByTuitionId method
     */
    @Test
    void Should_DeleteByTuitionId_When_DeletingByTuitionIdIsSuccessful() throws IOException {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenReturn(getSampleResponseEntity());
        paymentService.deleteByTuitionId(TUITION_ID, TOKEN);
        verify(restTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap());
    }

    @Test
    void Should_ThrowPaymentServiceHttpClientErrorException_When_DeletingByTuitionIdIsFailed() {
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class),
                anyMap())).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        PaymentServiceHttpClientErrorException exception = assertThrows(PaymentServiceHttpClientErrorException.class,
                () -> paymentService.deleteByTuitionId(TUITION_ID, TOKEN));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value() + " Failed to delete payments by tuitionId",
                exception.getMessage());
    }

    /**
     * This method returns a sample response entity
     *
     * @return Response Enity
     */
    private ResponseEntity<String> getSampleResponseEntity() {
        return new ResponseEntity<>("Response", HttpStatus.OK);
    }
}
