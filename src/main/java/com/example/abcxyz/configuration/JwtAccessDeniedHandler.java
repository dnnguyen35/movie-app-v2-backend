package com.example.abcxyz.configuration;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.enums.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;

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
