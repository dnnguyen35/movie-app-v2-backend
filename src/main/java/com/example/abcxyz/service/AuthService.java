package com.example.abcxyz.service;

import com.example.abcxyz.dto.request.*;
import com.example.abcxyz.dto.response.*;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface AuthService {

    RegisterResponse register(RegisterRequest registerRequest) throws MessagingException, IOException,
            TemplateException;

    VerifyOtpResponse verifyOtpAndCreateUser(VerifyOtpRequest verifyOtpRequest);

    LoginResponse login(LoginRequest loginRequest);

    ResendOtpResponse resendOtp(ResendOtpRequest resendOtpRequest) throws MessagingException, IOException, TemplateException;

    ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest) throws MessagingException,
            IOException, TemplateException;

    RenewAccessTokenResponse renewAccessToken(String refreshToken);

    void logout(String refreshToken);
}
