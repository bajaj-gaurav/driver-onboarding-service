package com.example.driveronboardingservice.service;

import com.example.driveronboardingservice.entity.DriverOnboardingStatusLog;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.repository.DriverOnboardingStatusLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DriverOnboardingStatusLogService {

    @Autowired
    private DriverOnboardingStatusLogRepository driverOnboardingStatusLogRepository;

    /**
     * Function to store onboarding status activity logs for the user.
     * @param driverProfile
     */
    public void storeOnboardingStatusLog(DriverProfile driverProfile) {
        log.info("Received a request to add user onboarding status log for user: {}", driverProfile.getEmail());
        DriverOnboardingStatusLog driverOnboardingStatusLog = new DriverOnboardingStatusLog();
        driverOnboardingStatusLog.setDriverProfile(driverProfile);
        driverOnboardingStatusLog.setTargetOnboardingStatus(driverProfile.getOnboardingStatus());
        driverOnboardingStatusLogRepository.save(driverOnboardingStatusLog);
    }

}
