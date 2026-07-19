package com.example.abcxyz.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@Slf4j
public class MailConfig {

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port}")
    private int mailPort;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.app-pass}")
    private String mailAppPass;

    @Value("${mail.auth}")
    private boolean mailAuth;

    @Value("${mail.start-tls}")
    private boolean mailStartTls;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(this.mailHost);
        javaMailSender.setPort(this.mailPort);
        javaMailSender.setUsername(this.mailUsername);
        javaMailSender.setPassword(this.mailAppPass);
        javaMailSender.setDefaultEncoding("UTF-8");

        Properties props = javaMailSender.getJavaMailProperties();

        props.put("mail.smtp.auth", this.mailAuth);
        props.put("mail.smtp.starttls.enable", this.mailStartTls);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.connectiontimeout", 5000);
        props.put("mail.smtp.timeout", 5000);
        props.put("mail.smtp.writetimeout", 5000);

        log.info("MailSender created successfully");

        return javaMailSender;
    }
}
