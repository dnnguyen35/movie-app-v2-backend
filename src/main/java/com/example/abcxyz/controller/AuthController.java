package com.example.abcxyz.controller;

import com.example.abcxyz.dto.ApiResponse;
import com.example.abcxyz.dto.request.*;
import com.example.abcxyz.dto.response.LoginResponse;
import com.example.abcxyz.dto.response.RegisterResponse;
import com.example.abcxyz.dto.response.RenewAccessTokenResponse;
import com.example.abcxyz.dto.response.VerifyOtpResponse;
import com.example.abcxyz.service.AuthService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest registerRequest) throws IOException, MessagingException, TemplateException {
        return ResponseEntity.ok(ApiResponse.success(200, "OTP has been send to email",
                this.authService.register(registerRequest)));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyOtpResponse>> verifyOtpAndCreateUser(@RequestBody @Valid VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.ok(ApiResponse.success(201, "Verify successfully",
                this.authService.verifyOtpAndCreateUser(verifyOtpRequest)));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success(200, "Login successfully", this.authService.login(loginRequest)));
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
    public ResponseEntity<ApiResponse<RenewAccessTokenResponse>> renewAccessToken(@RequestBody @Valid RenewAccessTokenRequest renewAccessTokenRequest) {
        return ResponseEntity.ok(ApiResponse.success(200, "Successfully",
                this.authService.renewAccessToken(renewAccessTokenRequest)));
    }
}
