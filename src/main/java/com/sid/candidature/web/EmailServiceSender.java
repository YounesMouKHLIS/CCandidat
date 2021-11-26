package com.sid.candidature.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceSender {
    @Autowired
    private JavaMailSender mailSender;

    public  void sendSimpleEmail(String toEmail,String body,String subject){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("rcrcrc-ba29aa@inbox.mailtrap.io");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }
}
