package com.example.driveronboardingservice.repository;

import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    EmailVerification findByDriverProfile(DriverProfile driverProfile);
}
