package com.example.driveronboardingservice.controller;

import com.example.driveronboardingservice.exception.EmailNotificationException;
import com.example.driveronboardingservice.service.EmailVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@Slf4j
public class EmailVerificationController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @GetMapping("/verify/email")
    public ResponseEntity<String> verifyEmail(@RequestParam("username") String username, @RequestParam("otp") String otp) {
        log.info("Received request for verifying email for user: {}", username);
        try {
            String message = emailVerificationService.verifyEmail(username, otp);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
        } catch (EmailNotificationException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
