package com.nkd.eida_backend.Service.Impl;

import com.nkd.eida_backend.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.nkd.eida_backend.Utils.EmailUtils.getRegistrationMessage;
import static com.nkd.eida_backend.Utils.EmailUtils.handleEmailException;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private static final String NEW_ACCOUNT_VERIFICATION = "New User Account Verification";
    private static final String PASSWORD_RESET_REQUEST = "Password Reset";
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.mail.verify.host}")
    private String host;

    @Override
    public void sendRegistrationEmail(String name, String email, String token) {
        try {
            sendEmail(email, getRegistrationMessage(name, host, token));
        } catch (Exception e) {
            handleEmailException(e);
        }
    }

    private void sendEmail(String to, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(EmailServiceImpl.NEW_ACCOUNT_VERIFICATION);
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setText(content);
        javaMailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String firstName, String token, String email) {

    }
}
