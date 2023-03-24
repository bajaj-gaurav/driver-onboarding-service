package com.example.driveronboardingservice.controller;

import com.example.driveronboardingservice.dto.DriverDetailsDto;
import com.example.driveronboardingservice.dto.DriverProfileDto;
import com.example.driveronboardingservice.exception.*;
import com.example.driveronboardingservice.service.DriverProfileService;
import com.example.driveronboardingservice.util.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@Slf4j
public class DriverProfileController {

    @Autowired
    private DriverProfileService driverProfileService;

    @PostMapping("/drivers/register")
    public ResponseEntity<String> registerDriver(@RequestBody DriverProfileDto driver) {
        log.info("Received a request to register driver with name:{} {}", driver.getFirstName(), driver.getLastName());
        try {
            String message = driverProfileService.saveProfile(driver);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (DriverAlreadyExistException | InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RoleNotFoundException | EmailNotificationException | MessagingException |
                 DriverRegistrationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/drivers/profile")
    public ResponseEntity<DriverDetailsDto> getDriverDetails() {
        log.info("Received request to get user profile");
        String username = UserAuthentication.getUserFromToken();
        log.info("User name {}", username);
        DriverDetailsDto user = null;
        try {
            user = driverProfileService.getDriverProfileDetails(username);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
