package com.example.driveronboardingservice.infrastructure.output;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.example.driveronboardingservice.constants.AppConstants.NOTIFICATION_SENDER;
import static com.example.driveronboardingservice.constants.AppConstants.NOTIFICATION_SUBJECT;

@Service
@Slf4j
public class MailServiceClient {

    private final JavaMailSender mailSender;

    @Autowired
    public MailServiceClient(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String body) throws MessagingException {
        log.info("Received request to send the messsage to:{}", to);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setText(body, true);
        helper.setTo(to);
        helper.setSubject(NOTIFICATION_SUBJECT);
        helper.setFrom(NOTIFICATION_SENDER);
        mailSender.send(mimeMessage);
    }
}

