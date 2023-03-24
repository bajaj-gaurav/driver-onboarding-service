package com.example.driveronboardingservice.repository;

import com.example.driveronboardingservice.entity.DocumentCategory;
import com.example.driveronboardingservice.entity.DocumentUpload;
import com.example.driveronboardingservice.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentUploadRepository extends JpaRepository<DocumentUpload, Long> {
    DocumentUpload findByFileHash(String fileHash);

    Optional<List<DocumentUpload>> findAllByDriverProfile(DriverProfile driverProfile);

    @Query("select d from DocumentUpload d where d.driverProfile = ?1 and d.documentCategory = ?2")
    DocumentUpload findByDriverProfileAndDocumentCategory(DriverProfile driverProfile, DocumentCategory documentCategory);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE DocumentUpload d SET d.fileHash = ?1, d.locationUrl = ?2 WHERE d.id = ?3")
    int updateDocument(String documentHash, String documentStoreUrl, Long docCategoryId);
}
