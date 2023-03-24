package com.example.driveronboardingservice.dto.mapper;

import com.example.driveronboardingservice.dto.DriverAdditionalInfoDto;
import com.example.driveronboardingservice.entity.DriverAdditionalInfo;
import org.springframework.stereotype.Component;

@Component
public class DriverAdditionalInfoMapper {

    public DriverAdditionalInfo mapDTOtoEntity(DriverAdditionalInfoDto driverAdditionalInfoDto) {
        DriverAdditionalInfo driverAdditionalInfo = new DriverAdditionalInfo();

        driverAdditionalInfo.setDrivingLicenseNo(driverAdditionalInfoDto.getDrivingLicenseNo());
        driverAdditionalInfo.setAadharNumber(driverAdditionalInfoDto.getAadharNumber());
        driverAdditionalInfo.setPancard(driverAdditionalInfoDto.getPancard());
        driverAdditionalInfo.setLanguagesKnown(driverAdditionalInfoDto.getLanguagesKnown());
        driverAdditionalInfo.setYearsOfDrivingExperience(driverAdditionalInfoDto.getYearsOfDrivingExperience());
        driverAdditionalInfo.setNationality(driverAdditionalInfoDto.getNationality());
        driverAdditionalInfo.setNominee(driverAdditionalInfoDto.getNominee());
        driverAdditionalInfo.setDateOfBirth(driverAdditionalInfoDto.getDateOfBirth());
        driverAdditionalInfo.setEmergencyContact(driverAdditionalInfoDto.getEmergencyContact());

        return driverAdditionalInfo;
    }
}
