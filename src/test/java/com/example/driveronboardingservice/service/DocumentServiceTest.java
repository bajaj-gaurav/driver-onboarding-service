package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.entity.DocumentCategory;
import com.example.driveronboardingservice.entity.DocumentUpload;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.exception.ActionNotAllowedException;
import com.example.driveronboardingservice.exception.InvalidInputException;
import com.example.driveronboardingservice.infrastructure.output.FileUploadClient;
import com.example.driveronboardingservice.repository.DocumentCategoryRepository;
import com.example.driveronboardingservice.repository.DocumentUploadRepository;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import com.example.driveronboardingservice.validation.DocumentValidation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@SpringBootTest
class DocumentServiceTest {

    @Autowired
    private DocumentService documentService;

    @MockBean
    private DocumentUploadRepository documentUploadRepository;

    @MockBean
    private DriverProfileRepository driverProfileRepository;

    @MockBean
    private DocumentCategoryRepository documentCategoryRepository;

    @MockBean
    private DocumentValidation documentValidation;

    @MockBean
    private FileUploadClient fileUploadClient;


    @Test
    public void testSuccessNewUploadSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setOnboardingStatus("abc");
        when(driverProfileRepository.findByEmail(any())).thenReturn(driverProfile);

        doNothing().when(documentValidation).isValidDocument(any());

        when(documentValidation.generateDocumentHash(any(), any())).thenReturn("abcd123");

        when(documentUploadRepository.findByFileHash(any())).thenReturn(null);

        when(documentCategoryRepository.findByName(any())).thenReturn(new DocumentCategory());

        when(documentUploadRepository.findByDriverProfileAndDocumentCategory(any(), any())).thenReturn(null);

        when(fileUploadClient.storeAndGetFileAddress(any(), any())).thenReturn("/c://somePath");

        DocumentUpload documentUpload = new DocumentUpload();
        documentUpload.setId(Long.valueOf(1));

        when(documentUploadRepository.save(any())).thenReturn(documentUpload);

        assertEquals("Document uploaded successfully", documentService.saveDocument(multipartFile, "samiksha", "address"));
    }

    @Test
    public void testSuccessReUploadSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setOnboardingStatus("abc");
        when(driverProfileRepository.findByEmail(any())).thenReturn(driverProfile);

        doNothing().when(documentValidation).isValidDocument(any());

        when(documentValidation.generateDocumentHash(any(), any())).thenReturn("abcd123");

        when(documentUploadRepository.findByFileHash(any())).thenReturn(null);

        when(documentCategoryRepository.findByName(any())).thenReturn(new DocumentCategory());

        when(documentUploadRepository.findByDriverProfileAndDocumentCategory(any(), any())).thenReturn(new DocumentUpload());

        when(documentUploadRepository.updateDocument(any(), any(), any())).thenReturn(1);

        assertEquals("Document re-uploaded successfully", documentService.saveDocument(multipartFile, "samiksha", "address"));
    }

    @Test
    public void testFailureReUploadSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setOnboardingStatus("abc");
        when(driverProfileRepository.findByEmail(any())).thenReturn(driverProfile);

        doNothing().when(documentValidation).isValidDocument(any());

        when(documentValidation.generateDocumentHash(any(), any())).thenReturn("abcd123");

        when(documentUploadRepository.findByFileHash(any())).thenReturn(null);

        when(documentCategoryRepository.findByName(any())).thenReturn(new DocumentCategory());

        when(documentUploadRepository.findByDriverProfileAndDocumentCategory(any(), any())).thenReturn(new DocumentUpload());

        when(documentUploadRepository.updateDocument(any(), any(), any())).thenReturn(0);

        assertThrows(Exception.class, () -> documentService.saveDocument(multipartFile, "samiksha", "address"), " Error Uploading document. Please try again in a while");

    }

    @Test
    public void testFailureUnknownUserSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        when(driverProfileRepository.findByEmail(any())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> documentService.saveDocument(multipartFile, "samiksha", "address"), "Couldn't find the user samiksha");

    }

    @Test
    public void testFailureActionNotAllowedSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setOnboardingStatus("BACKGROUND_CHECK_IN_PROGRESS");
        when(driverProfileRepository.findByEmail(any())).thenReturn(driverProfile);

        assertThrows(ActionNotAllowedException.class, () -> documentService.saveDocument(multipartFile, "samiksha", "address"), "Background Verification is in process. Please wait for the verification to get complete.");

    }

    @Test
    public void testFailureDuplicateDocumentSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setOnboardingStatus("abc");
        when(driverProfileRepository.findByEmail(any())).thenReturn(driverProfile);

        doNothing().when(documentValidation).isValidDocument(any());

        when(documentValidation.generateDocumentHash(any(), any())).thenReturn("abcd123");

        when(documentUploadRepository.findByFileHash(any())).thenReturn(new DocumentUpload());

        assertThrows(InvalidInputException.class, () -> documentService.saveDocument(multipartFile, "samiksha", "address"), "Duplicate file found");
    }

    @Test
    public void testFailureSaveDocument() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("mockFile.pdf", "originalFileName.pdf", MediaType.TEXT_PLAIN_VALUE, "hello".getBytes());

        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setOnboardingStatus("abc");
        when(driverProfileRepository.findByEmail(any())).thenReturn(driverProfile);

        doNothing().when(documentValidation).isValidDocument(any());

        when(documentValidation.generateDocumentHash(any(), any())).thenReturn("abcd123");

        when(documentUploadRepository.findByFileHash(any())).thenReturn(null);

        when(documentCategoryRepository.findByName(any())).thenReturn(new DocumentCategory());

        when(documentUploadRepository.findByDriverProfileAndDocumentCategory(any(), any())).thenReturn(null);

        when(fileUploadClient.storeAndGetFileAddress(any(), any())).thenReturn("/c://somePath");

        when(documentUploadRepository.save(any())).thenReturn(new DocumentUpload());

        assertThrows(Exception.class, () -> documentService.saveDocument(multipartFile, "samiksha", "address"), "Error Uploading document. Please try again in a while");
    }

}