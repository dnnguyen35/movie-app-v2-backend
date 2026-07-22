package com.example.abcxyz.controller;

import com.example.abcxyz.configuration.RefreshTokenCookieProvider;
import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.dto.request.*;
import com.example.abcxyz.dto.response.LoginResponse;
import com.example.abcxyz.dto.response.RegisterResponse;
import com.example.abcxyz.dto.response.RenewAccessTokenResponse;
import com.example.abcxyz.dto.response.VerifyOtpResponse;
import com.example.abcxyz.service.AuthService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public AuthController(AuthService authService, RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.authService = authService;
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest registerRequest) throws IOException, MessagingException, TemplateException {
        return ResponseEntity.ok(ApiResponse.success(200, "OTP has been send to email",
                this.authService.register(registerRequest)));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtpAndCreateUser(@RequestBody @Valid VerifyOtpRequest verifyOtpRequest, HttpServletResponse response) {
        VerifyOtpResponse verifyOtpResponse = this.authService.verifyOtpAndCreateUser(verifyOtpRequest);

        ResponseCookie cookie = this.refreshTokenCookieProvider.create(verifyOtpResponse.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        verifyOtpResponse.setRefreshToken(null);

        return ResponseEntity.ok(ApiResponse.success(201, "Verify otp successfully",
                verifyOtpResponse));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest,
                                                            HttpServletResponse response) {
        LoginResponse loginResponse = this.authService.login(loginRequest);

        ResponseCookie cookie = this.refreshTokenCookieProvider.create(loginResponse.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        loginResponse.setRefreshToken(null);

        return ResponseEntity.ok(ApiResponse.success(200, "Login successfully", loginResponse));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<?>> resendOtp(@RequestBody @Valid ResendOtpRequest resendOtpRequest) throws MessagingException, IOException, TemplateException {
        return ResponseEntity.ok(ApiResponse.success(200, "Reset password successfully",
                this.authService.resendOtp(resendOtpRequest)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) throws MessagingException, IOException, TemplateException {
        return ResponseEntity.ok(ApiResponse.success(200, "Reset password successfully",
                this.authService.resetPassword(resetPasswordRequest)));
    }

    @PostMapping("/renew-token")
    public ResponseEntity<ApiResponse<RenewAccessTokenResponse>> renewAccessToken(@CookieValue(value = "refreshToken"
            , required = false) String refreshToken) {
        return ResponseEntity.ok(ApiResponse.success(200, "Renew access token successfully",
                this.authService.renewAccessToken(refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        this.authService.logout(refreshToken);

        ResponseCookie cookie = this.refreshTokenCookieProvider.clear();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.success(200, "Log out successfully", null));
    }
}
