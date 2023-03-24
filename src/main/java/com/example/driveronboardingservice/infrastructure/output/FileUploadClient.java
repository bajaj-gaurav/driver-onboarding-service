package com.example.driveronboardingservice.infrastructure.output;

import com.example.driveronboardingservice.exception.FileStoreException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUploadClient {

    public String storeAndGetFileAddress(String directoryPath, MultipartFile file) throws FileStoreException {
        String fileName = file.getOriginalFilename();

        // Create the directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create the file and write some content to it
        Path filePath = Paths.get(directoryPath, fileName);

        try {
            byte[] fileContent = file.getBytes();
            Files.write(filePath, fileContent);
        } catch (IOException e) {
            throw new FileStoreException("Couldn't upload the file. Please try again after some time");
        }

        // Get the full path of the file
        return filePath.toAbsolutePath().toString();
    }
}
