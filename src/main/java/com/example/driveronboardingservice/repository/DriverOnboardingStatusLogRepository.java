package com.example.driveronboardingservice.repository;

import com.example.driveronboardingservice.entity.DriverOnboardingStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverOnboardingStatusLogRepository extends JpaRepository<DriverOnboardingStatusLog, Long> {

}
