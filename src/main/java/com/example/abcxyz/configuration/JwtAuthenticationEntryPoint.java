package com.example.abcxyz.configuration;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.enums.ErrorCode;
import com.example.abcxyz.exception.AppException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        AppException ex = (AppException) request.getAttribute("accessTokenError");

        ErrorCode errorCode = ex != null ? ex.getErrorCode() : ErrorCode.UNAUTHORIZED;

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        ApiResponse<?> apiResponse = ApiResponse.error(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                errorCode.name()
        );

        ObjectMapper om = new ObjectMapper();

        response.getWriter().write(om.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
