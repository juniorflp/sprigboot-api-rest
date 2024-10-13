package com.example.curso_api_rest_java.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType("application/json;charset=UTF-8");

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                "Access Denied: You do not have permission to access this resource",
                request.getRequestURI()
        );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(exceptionResponse);

        response.getWriter().write(json);
    }
}