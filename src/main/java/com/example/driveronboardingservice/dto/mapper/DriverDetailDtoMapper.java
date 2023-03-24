package com.example.driveronboardingservice.dto.mapper;

import com.example.driveronboardingservice.dto.DriverDetailsDto;
import com.example.driveronboardingservice.entity.DriverAdditionalInfo;
import com.example.driveronboardingservice.entity.DriverProfile;
import org.springframework.stereotype.Component;

@Component
public class DriverDetailDtoMapper {

    public void mapDriverProfileToDriverDetailDto(DriverDetailsDto driverDetailsDto, DriverProfile driverProfile) {
        driverDetailsDto.setEmail(driverProfile.getEmail());
        driverDetailsDto.setFirstName(driverProfile.getFirstName());
        driverDetailsDto.setLastName(driverProfile.getLastName());
        driverDetailsDto.setOnboardingStatus(driverProfile.getOnboardingStatus());
        driverDetailsDto.setAvailabilityStatus(driverProfile.getAvailabilityStatus());
        driverDetailsDto.setPhoneNo(driverProfile.getPhoneNo());
        driverDetailsDto.setEnabledStatus(driverProfile.getEnabled());
        driverDetailsDto.setCity(driverProfile.getCity());
        driverDetailsDto.setState(driverProfile.getState());
    }

    public void mapDriverAdditionalInfoToDriverDetailDto(DriverDetailsDto driverDetailsDto, DriverAdditionalInfo driverAdditionalInfo) {
        driverDetailsDto.setPancard(driverAdditionalInfo.getPancard());
        driverDetailsDto.setAadharNumber(driverAdditionalInfo.getAadharNumber());
        driverDetailsDto.setDrivingLicenseNo(driverAdditionalInfo.getDrivingLicenseNo());
        driverDetailsDto.setDateOfBirth(driverAdditionalInfo.getDateOfBirth());
        driverDetailsDto.setEmergencyContact(driverAdditionalInfo.getEmergencyContact());
        driverDetailsDto.setYearsOfDrivingExperience(driverAdditionalInfo.getYearsOfDrivingExperience());
        driverDetailsDto.setLanguagesKnown(driverAdditionalInfo.getLanguagesKnown());
        driverDetailsDto.setNominee(driverAdditionalInfo.getNominee());
        driverDetailsDto.setNationality(driverAdditionalInfo.getNationality());
    }
}