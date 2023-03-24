package com.example.driveronboardingservice.validation;

import com.example.driveronboardingservice.exception.FileHashCreationFailedException;
import com.example.driveronboardingservice.exception.InvalidInputException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

import static com.example.driveronboardingservice.constants.AppConstants.MAX_FILE_SIZE_LIMIT;
import static com.example.driveronboardingservice.constants.AppConstants.VALID_FILE_EXTENSIONS;

@Component
@Slf4j
public class DocumentValidation {


    private String generateHash(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        String checksum = DigestUtils.sha256Hex(is);
        is.close();

        return checksum;
    }


    private String getFileType(MultipartFile file) {
        String fileType = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        return fileType;
    }

    private Boolean isFileNameValid(String filename) {
        if (filename.contains("..") || filename.contains("/")) {
            return false;
        }

        // Check for reserved characters
        String[] reservedChars = new String[]{"<", ">", ":", "\"", "/", "\\", "|", "?", "*"};
        for (String reservedChar : reservedChars) {
            if (filename.contains(reservedChar)) {
                return false;
            }
        }

        // Check for control characters
        for (int i = 0; i < filename.length(); i++) {
            if (Character.isISOControl(filename.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private Boolean isValidFileSize(MultipartFile file) {
        return file.getSize() <= MAX_FILE_SIZE_LIMIT;
    }

    private String getFileExtension(String fileType) {
        Optional<String> extension = VALID_FILE_EXTENSIONS.stream().filter(fileType::equals).findFirst();
        return extension.orElse("");
    }

    private Boolean isValidFileType(MultipartFile file) {
        String fileType = getFileType(file);
        String extension = getFileExtension(fileType);
        return !extension.isEmpty();
    }

    public String generateDocumentHash(MultipartFile file, String username) throws FileHashCreationFailedException {
        String fileHash = "";
        String fileType = getFileType(file);

        try {
            if (!fileType.isEmpty()) {
                String checksum = generateHash(file);
                long fileSize = file.getSize();
                fileHash = checksum + fileType + fileSize + username;
            }
        } catch (Exception e) {
            log.error("Couldn't upload the doc with name {}", file.getOriginalFilename(), e);
            throw new FileHashCreationFailedException("Couldn't upload the doc. Please try again after some time");
        }
        return fileHash;
    }

    public void isValidDocument(MultipartFile file) throws InvalidInputException {
        if (file.isEmpty()) {
            log.error("No file present");
            throw new InvalidInputException("No file present");

        }

        if (!isFileNameValid(Objects.requireNonNull(file.getOriginalFilename()))) {
            log.error("Invalid filename:{} present", file.getOriginalFilename());
            throw new InvalidInputException("Invalid filename present");
        }


        if (!isValidFileSize(file)) {
            log.error("File size exceeds 5MB limit");
            throw new InvalidInputException("File size exceeds 5MB limit");
        }

        if (!isValidFileType(file)) {
            log.error("Invalid file type present for file:{}", file.getOriginalFilename());
            throw new InvalidInputException("Invalid file type present. Supported file types are .png/.jpg.jpeg/.pdf/.zip");
        }
    }

}
