package com.example.driveronboardingservice.repository;

import com.example.driveronboardingservice.entity.DriverAdditionalInfo;
import com.example.driveronboardingservice.entity.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverAdditionalInfoRepository extends JpaRepository<DriverAdditionalInfo, Long> {

    DriverAdditionalInfo findByDriverProfile(DriverProfile driverProfile);


}
