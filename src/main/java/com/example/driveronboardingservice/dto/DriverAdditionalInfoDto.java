package com.example.driveronboardingservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DriverAdditionalInfoDto {

    @JsonProperty(value = "driving_license_no", required = true)
    private String drivingLicenseNo;

    @JsonProperty(value = "aadhar_number", required = true)
    private String aadharNumber;

    @JsonProperty(value = "years_of_driving_experience", required = true)
    private String yearsOfDrivingExperience;

    @JsonProperty(value = "nominee", required = true)
    private String nominee;

    @JsonProperty(value = "languages_known", required = true)
    private String languagesKnown;

    @JsonProperty(value = "pancard", required = true)
    private String pancard;

    @JsonProperty(value = "nationality", required = true)
    private String nationality;

    @JsonProperty(value = "date_of_brith", required = true)
    private String dateOfBirth;

    @JsonProperty(value = "emergency_contact", required = true)
    private String emergencyContact;

}
