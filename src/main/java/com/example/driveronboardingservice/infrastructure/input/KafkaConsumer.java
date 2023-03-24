package com.example.driveronboardingservice.infrastructure.input;

import com.example.driveronboardingservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSendException;

//@Service
@Slf4j
public class KafkaConsumer {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "driver-events", groupId = "group_id", concurrency = "1")
    public void consume(String message) {
        log.info("Received request to send notification for message: {}", message);
        try {
            notificationService.sendMail(message);
        } catch (MailSendException e) {
            log.error("Couldn't send the message: {} ", message, e);
        }

    }
}
