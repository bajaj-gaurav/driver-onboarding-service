package com.example.driveronboardingservice.controller;

import com.example.driveronboardingservice.exception.*;
import com.example.driveronboardingservice.service.DocumentService;
import com.example.driveronboardingservice.util.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/driver/documents")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("category") String category) {
        log.info("Received request to upload document {}", file.getOriginalFilename());
        String username = UserAuthentication.getUserFromToken();
        log.info("Username {}", username);
        try {
            String message = documentService.saveDocument(file, username, category);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
        } catch (InvalidInputException | ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DatabaseException | FileHashCreationFailedException | FileStoreException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
