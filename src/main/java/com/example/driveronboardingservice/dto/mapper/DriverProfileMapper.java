package com.example.driveronboardingservice.dto.mapper;

import com.example.driveronboardingservice.dto.DriverProfileDto;
import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.model.DriverAvailabilityStatus;
import com.example.driveronboardingservice.model.DriverOnboardingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DriverProfileMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DriverProfile mapDTOtoEntity(DriverProfileDto driverProfileDto) {
        DriverProfile driverProfile = new DriverProfile();
        driverProfile.setFirstName(driverProfileDto.getFirstName());
        driverProfile.setLastName(driverProfileDto.getLastName());
        driverProfile.setEmail(driverProfileDto.getEmail());
        driverProfile.setPhoneNo(driverProfileDto.getPhoneNo());
        driverProfile.setCity(driverProfileDto.getCity());
        driverProfile.setState(driverProfileDto.getState());

        driverProfile.setPassword(hashPassword(driverProfileDto.getPassword()));

        driverProfile.setOnboardingStatus(DriverOnboardingStatus.DOCUMENT_PENDING.name());
        driverProfile.setAvailabilityStatus(DriverAvailabilityStatus.INACTIVE.name());
        driverProfile.setEnabled(false);
        return driverProfile;
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
