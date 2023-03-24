package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.entity.DocumentCategory;
import com.example.driveronboardingservice.entity.DocumentUpload;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.exception.*;
import com.example.driveronboardingservice.infrastructure.output.FileUploadClient;
import com.example.driveronboardingservice.model.DriverOnboardingStatus;
import com.example.driveronboardingservice.repository.DocumentCategoryRepository;
import com.example.driveronboardingservice.repository.DocumentUploadRepository;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import com.example.driveronboardingservice.validation.DocumentValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class DocumentService {


    private static final String DIRECTORY_PATH = "uploaded-files";
    @Autowired
    private DocumentUploadRepository documentUploadRepository;

    @Autowired
    private DriverProfileRepository driverProfileRepository;

    @Autowired
    private DocumentCategoryRepository documentCategoryRepository;

    @Autowired
    private DocumentValidation documentValidation;

    @Autowired
    private FileUploadClient fileUploadClient;


    /**
     * Function to check if the same document already exists in the database.
     * @param fileHash
     * @throws InvalidInputException
     */
    private void isDuplicateDocument(String fileHash) throws InvalidInputException {
        DocumentUpload docUp = documentUploadRepository.findByFileHash(fileHash);
        if (docUp != null) {
            log.info("Found already uploaded doc with id: {}", docUp.getId());
            throw new InvalidInputException("Duplicate file found");
        }
    }


    /**
     * Function to save the document uploaded by user if all the validations are passed.
     * @param file
     * @param username
     * @param category
     * @return
     * @throws DatabaseException
     * @throws FileStoreException
     * @throws InvalidInputException
     * @throws FileHashCreationFailedException
     * @throws ActionNotAllowedException
     */
    public String saveDocument(MultipartFile file, String username, String category) throws DatabaseException, FileStoreException, InvalidInputException, FileHashCreationFailedException, ActionNotAllowedException {
        // Check if the file is empty

        DriverProfile driverProfile = driverProfileRepository.findByEmail(username);

        if (driverProfile == null) {
            log.error("Couldn't find the user {}", username);
            throw new UsernameNotFoundException("Couldn't find the user " + username);
        }

        if (driverProfile.getOnboardingStatus().equals(DriverOnboardingStatus.BACKGROUND_CHECK_IN_PROGRESS.name())) {
            log.error("Background Verification is in process for user: {}", username);
            throw new ActionNotAllowedException("Background Verification is in process. " +
                    "Please wait for the verification to get complete.");
        }

        documentValidation.isValidDocument(file);
        log.info("Document validated for user: {} with category{}", username, category);
        String documentHash = documentValidation.generateDocumentHash(file, username);
        isDuplicateDocument(documentHash);

        DocumentCategory docCategory = documentCategoryRepository.findByName(category);
        if (docCategory == null) {
            log.error("Category: {} doesn't exist", category);
            throw new ActionNotAllowedException("Category doesn't exist");
        }
        DocumentUpload documentUpload = documentUploadRepository.findByDriverProfileAndDocumentCategory(driverProfile, docCategory);

        String directoryPath = DIRECTORY_PATH + "/" + username + "/" + category;
        log.info("Storing the doc in location:{}", directoryPath);
        String documentStoreUrl = fileUploadClient.storeAndGetFileAddress(directoryPath, file);

        if (documentUpload == null) {
            documentUpload = new DocumentUpload();
            documentUpload.setFileHash(documentHash);
            documentUpload.setDriverProfile(driverProfile);
            documentUpload.setLocationUrl(documentStoreUrl);
            documentUpload.setDocumentCategory(docCategory);

            DocumentUpload savedDocumentUpload = documentUploadRepository.save(documentUpload);
            if (savedDocumentUpload.getId() != null) {
                return "Document uploaded successfully";
            } else {
                log.error("Error Uploading document for user:{} with category:{}", username, category);
                throw new DatabaseException("Error Uploading document. Please try again in a while");
            }
        } else {
            int uploadStatus = documentUploadRepository.updateDocument(documentHash, documentStoreUrl, documentUpload.getId());
            if (uploadStatus == 1) {
                return "Document re-uploaded successfully";
            } else {
                log.error("Error Uploading document for user:{} with category:{}", username, category);
                throw new DatabaseException("Error Uploading document. Please try again in a while");
            }
        }

    }
}
