package com.example.driveronboardingservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "drivers", indexes = {@Index(name = "driver_email_index", columnList = "email", unique = true), @Index(name = "first_name_index", columnList = "first_name"), @Index(name = "enabled_index", columnList = "enabled"), @Index(name = "city_index", columnList = "city"), @Index(name = "state_index", columnList = "state")})
@Data
public class DriverProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "phone_no", nullable = false)
    private String phoneNo;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "onboarding_status", nullable = false)
    private String onboardingStatus;

    @Column(name = "availability_status", nullable = false)
    private String availabilityStatus;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;


}


