package com.example.abcxyz.service.implement;

import com.example.abcxyz.service.EmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final Configuration freeMarkerConfig;

    public EmailServiceImpl(JavaMailSender javaMailSender, Configuration freeMarkerConfig) {
        this.javaMailSender = javaMailSender;
        this.freeMarkerConfig = freeMarkerConfig;
    }

    @Async("mailTaskExecutor")
    @Override
    public void sendOtpEmail(String emailTarget, String otpCode) throws MessagingException,
            IOException, TemplateException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Map<String, String> data = new HashMap<>();
        data.put("appName", "phimCuaToi");
        data.put("otpCode", otpCode);

        Template template = this.freeMarkerConfig.getTemplate("otp-template.ftlh");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        mimeMessageHelper.setTo(emailTarget);
        mimeMessageHelper.setSubject("OTP for register");
        mimeMessageHelper.setText(htmlContent, true);

        this.javaMailSender.send(mimeMessage);

        log.info("OtpEmail has been send successfully to: {}", emailTarget);
    }

    @Async("mailTaskExecutor")
    @Override
    public void sendResetPasswordEmail(String emailTarget, String newPassword) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        Map<String, String> data = new HashMap<>();
        data.put("appName", "phimCuaToi");
        data.put("newPassword", newPassword);

        Template template = this.freeMarkerConfig.getTemplate("reset-password-template.ftlh");
        String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

        mimeMessageHelper.setTo(emailTarget);
        mimeMessageHelper.setSubject("Reset password for phimCuaToi");
        mimeMessageHelper.setText(htmlContent, true);

        this.javaMailSender.send(mimeMessage);

        log.info("Reset password successfully for: {}", emailTarget);
    }
}
