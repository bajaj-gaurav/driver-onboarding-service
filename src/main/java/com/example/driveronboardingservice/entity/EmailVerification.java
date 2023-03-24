package com.example.driveronboardingservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "email_verification")
@Data
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String otp;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driverProfile;

    @Column(name = "otp_generated_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp otpGeneratedTime;

}
