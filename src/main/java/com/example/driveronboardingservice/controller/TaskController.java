package com.example.driveronboardingservice.controller;

import com.example.driveronboardingservice.dto.DriverAdditionalInfoDto;
import com.example.driveronboardingservice.exception.ActionNotAllowedException;
import com.example.driveronboardingservice.exception.DatabaseException;
import com.example.driveronboardingservice.service.TaskService;
import com.example.driveronboardingservice.util.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/drivers/onboarding/additional-info")
    public ResponseEntity<String> verifyDocuments(@RequestBody DriverAdditionalInfoDto driverAdditionalInfo) {
        log.info("Received request for submitted the additional info and start background verification");
        try {
            String username = UserAuthentication.getUserFromToken();
            log.info("User name {}", username);
            String message = taskService.saveDriverProfileAdditionalInfo(username, driverAdditionalInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DatabaseException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
