package com.swivel.ignite.tuition.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class customizes access denied exception responses on resource server endpoints
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final String ERROR_CODE = "errorCode";
    private static final String STATUS = "status";
    private static final String ERROR_STATUS = "ERROR";
    private static final String MESSAGE = "message";
    private static final String DISPLAY_MESSAGE = "displayMessage";
    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";
    private static final String APPLICATION_JSON = "application/json";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        Map<Object, Object> map = new HashMap<>();
        map.put(STATUS, ERROR_STATUS);
        map.put(MESSAGE, exception.getMessage());
        map.put(DISPLAY_MESSAGE, ERROR_MESSAGE);
        map.put(ERROR_CODE, HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType(APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), map);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
