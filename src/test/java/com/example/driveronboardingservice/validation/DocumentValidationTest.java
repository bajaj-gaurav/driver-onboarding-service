package com.example.driveronboardingservice.validation;

import com.example.driveronboardingservice.exception.FileHashCreationFailedException;
import com.example.driveronboardingservice.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DocumentValidationTest {

    private static MultipartFile multipartFile;

    @Autowired
    private DocumentValidation documentValidation;

    @BeforeEach
    public void setUp() {
        multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
    }

    @Test
    public void testSuccessGenerateDocumentHash() throws FileHashCreationFailedException {
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824pdf5lucky", documentValidation.generateDocumentHash(multipartFile, "lucky"));
    }

    @Test
    public void testFailureWithSameFileAndDifferentUserNameGenerateDocumentHash() throws FileHashCreationFailedException {
        assertNotEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824pdf5luc", documentValidation.generateDocumentHash(multipartFile, "gaurav"));
    }

    @Test
    public void testSuccessIsValidDocument() {
        assertDoesNotThrow(() -> documentValidation.isValidDocument(multipartFile));
    }

    @Test
    public void testFailureEmptyFileIsValidDocument() {
        multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "".getBytes());
        assertThrows(InvalidInputException.class, () -> documentValidation.isValidDocument(multipartFile), "No file present");
    }

    @Test
    public void testFailureInvalidFileNameIsValidDocument() {
        multipartFile = new MockMultipartFile("mock<File.pdf", "original<FileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
        assertThrows(InvalidInputException.class, () -> documentValidation.isValidDocument(multipartFile), "Invalid filename present");
    }

    @Test
    public void testFailureInvalidFileTypeIsValidDocument() {
        multipartFile = new MockMultipartFile("mockFile.xls", "originalFileName.xls", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());
        assertThrows(InvalidInputException.class, () -> documentValidation.isValidDocument(multipartFile), "Invalid file type present. Supported file types are .png/.jpg.jpeg/.pdf/.zip");
    }

}