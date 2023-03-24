package com.example.driveronboardingservice.infrastructure.output;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

//@Service
@Slf4j
public class KafkaProducerClient {
    private static final String TOPIC = "driver-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("Sending event {}", message);
        this.kafkaTemplate.send(TOPIC, message);
    }
}
