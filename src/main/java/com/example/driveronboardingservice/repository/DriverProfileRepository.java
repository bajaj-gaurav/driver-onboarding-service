package com.example.driveronboardingservice.repository;

import com.example.driveronboardingservice.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Driver repository to fetch details for the drivers
 * The primary key data type is Long which is also defined in the Driver entity
 */
@Repository
public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    DriverProfile findByEmail(String email);


    @Query("SELECT COUNT(*) = (SELECT COUNT(*) FROM DocumentCategory) FROM DriverProfile d LEFT JOIN DocumentUpload du ON d.id = du.driverProfile WHERE d.id = ?1 GROUP BY d.id")
    Boolean checkDocumentUpload(Long driverId);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE DriverProfile d SET d.onboardingStatus = ?2 WHERE d.id = ?1")
    void changeAction(Long driverId, String action);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE DriverProfile d SET d.enabled = ?2 WHERE d.id = ?1")
    void changeEnabledStatus(Long driverId, Boolean state);


}

