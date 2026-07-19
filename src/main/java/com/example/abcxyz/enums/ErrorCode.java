package com.example.abcxyz.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    REGISTER_SESSION_EXPIRED("Session expired! Please register again", HttpStatus.BAD_REQUEST),

    REVIEW_NOT_FOUND("Review not found", HttpStatus.NOT_FOUND),
    FAVORITE_NOT_FOUND("Favorite not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    MOVIE_NOT_FOUND("Movie not found", HttpStatus.NOT_FOUND),

    USER_LOCKED("User account has been locked", HttpStatus.FORBIDDEN),

    EMAIL_ALREADY_EXISTS("Email already exists", HttpStatus.CONFLICT),

    INVALID_INPUT("Data not valid", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD("Invalid password", HttpStatus.BAD_REQUEST),

    INVALID_OTP("Invalid OTP", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED("OTP expired", HttpStatus.BAD_REQUEST),

    INVALID_ACCESS_TOKEN("Invalid access token", HttpStatus.UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("Access token expired", HttpStatus.UNAUTHORIZED),

    INVALID_REFRESH_TOKEN("Invalid refresh token", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("Refresh token expired", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED("Unauthorized", HttpStatus.UNAUTHORIZED),

    FORBIDDEN("Permission denied", HttpStatus.FORBIDDEN),
    DISABLED_USER("User is disabled", HttpStatus.FORBIDDEN),

    METHOD_NOT_ALLOWED("Request method is not allowed", HttpStatus.METHOD_NOT_ALLOWED),

    BAD_REQUEST("Bad request", HttpStatus.BAD_REQUEST),

    UNSUPPORTED_MEDIA_TYPE("Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    RESET_PASSWORD_LIMITED("Your reset time has been limited", HttpStatus.TOO_MANY_REQUESTS),

    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    private ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
