package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.infrastructure.output.MailServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    MailServiceClient mailServiceClient;


    public String sendMail(String message) throws MailSendException {
        String[] events = message.split(":", 2);
        if (!events[0].isEmpty() && !events[1].isEmpty()) {
            String eventMessage = events[1].replaceAll("_", " ");
            //String body = "This is to inform that your onboarding status has changed to " + eventMessage;
            try {
                mailServiceClient.sendEmail(events[0].strip(), eventMessage);
            } catch (Exception e) {
                throw new MailSendException("Couldn't send the message: " + message);
            }
        }

        return "Mail successfully sent";
    }
}
