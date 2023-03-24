package com.example.driveronboardingservice.dto;

import lombok.Data;

@Data
public class DriverDetailsDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNo;

    private String city;

    private String state;

    private String drivingLicenseNo;

    private String aadharNumber;

    private String yearsOfDrivingExperience;

    private String nominee;

    private String languagesKnown;

    private String pancard;

    private String nationality;

    private String dateOfBirth;

    private String emergencyContact;

    private String onboardingStatus;

    private Boolean enabledStatus;

    private String availabilityStatus;

}
