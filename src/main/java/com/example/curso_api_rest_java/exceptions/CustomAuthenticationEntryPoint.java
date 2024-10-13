package com.example.curso_api_rest_java.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                "Unauthenticated: You need to log in to access this feature",
                request.getRequestURI()
        );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(exceptionResponse);

        response.getWriter().write(json);
    }
}