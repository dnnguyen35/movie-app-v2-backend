package com.example.abcxyz.exception;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        ApiResponse.error(
                                errorCode.getHttpStatus().value(),
                                errorCode.getMessage(),
                                errorCode.name()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : fieldError.getField() +
                                                                                                                " not valid",
                        (existsErrorMessage, newErrorMessage) -> existsErrorMessage
                ));

        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(
                        ApiResponse.validateError(
                                ErrorCode.INVALID_INPUT.getHttpStatus().value(),
                                ErrorCode.INVALID_INPUT.getMessage(),
                                ErrorCode.INVALID_INPUT.name(),
                                errorMessages
                        )
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(ErrorCode.FORBIDDEN.getHttpStatus())
                .body(
                        ApiResponse.error(
                                ErrorCode.FORBIDDEN.getHttpStatus().value(),
                                ErrorCode.FORBIDDEN.getMessage(),
                                ErrorCode.FORBIDDEN.name()
                        )
                );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity
                .status(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus())
                .body(
                        ApiResponse.error(
                                ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus().value(),
                                ErrorCode.METHOD_NOT_ALLOWED.getMessage(),
                                ErrorCode.METHOD_NOT_ALLOWED.name()
                        )
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getHttpStatus())
                .body(
                        ApiResponse.error(
                                ErrorCode.INVALID_INPUT.getHttpStatus().value(),
                                ErrorCode.INVALID_INPUT.getMessage(),
                                ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String supportMediaType = ex.getSupportedMediaTypes().stream().map(
                mediaType -> mediaType.toString()
        ).collect(Collectors.joining(","));

        String errorMessage = String.format("Only accept type: [%s]", supportMediaType);

        return ResponseEntity
                .status(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatus())
                .body(
                        ApiResponse.error(
                                ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatus().value(),
                                errorMessage,
                                ErrorCode.UNSUPPORTED_MEDIA_TYPE.name()
                        )
                );
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ApiResponse<?>> handleCompletableFutureException(CompletionException ex) {
        log.error("CompletableFuture unexpected error", ex.getCause());

        return handleAppException(new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("Internal server error", ex);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(
                        ApiResponse.error(
                                ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value(),
                                ErrorCode.INTERNAL_SERVER_ERROR.getMessage(),
                                ErrorCode.INTERNAL_SERVER_ERROR.name()
                        )
                );
    }
}
