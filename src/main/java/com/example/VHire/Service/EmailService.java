package com.example.vHire.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

@Autowired
    public JavaMailSender javaMailSender;


    public void sendHtmlEmail(String to, String subject, String htmlBody) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom("s05357259@gmail.com");

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}