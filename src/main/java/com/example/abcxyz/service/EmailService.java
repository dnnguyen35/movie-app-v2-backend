package com.example.abcxyz.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {

    void sendOtpEmail(String emailTarget, String otpCode) throws MessagingException,
            IOException, TemplateException;

    void sendResetPasswordEmail(String emailTarget, String newPassword) throws MessagingException, IOException,
            TemplateException;
}
