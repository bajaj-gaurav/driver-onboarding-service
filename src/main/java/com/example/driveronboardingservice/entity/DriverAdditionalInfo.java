package com.example.driveronboardingservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "driver_additional_info")
@Data
public class DriverAdditionalInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driving_license_no", nullable = false, unique = true)
    private String drivingLicenseNo;

    @Column(name = "aadhar_number", nullable = false, unique = true)
    private String aadharNumber;

    @Column(name = "years_of_driving_experience", nullable = false)
    private String yearsOfDrivingExperience;

    @Column(name = "nominee", nullable = false)
    private String nominee;

    @Column(name = "languages_known", nullable = false)
    private String languagesKnown;

    @Column(name = "pancard", nullable = false, unique = true)
    private String pancard;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "date_of_brith", nullable = false)
    private String dateOfBirth;

    @Column(name = "emergency_contact", nullable = false)
    private String emergencyContact;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driverProfile;

}
