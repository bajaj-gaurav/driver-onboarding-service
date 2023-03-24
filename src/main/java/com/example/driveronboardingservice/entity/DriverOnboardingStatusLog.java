package com.example.driveronboardingservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "driver_onboarding_status_log")
@Data
public class DriverOnboardingStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driverProfile;

    @Column(name = "task_status_change_date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp taskStatusChangeDateTime;

    @Column(name = "target_onboarding_status", nullable = false)
    private String targetOnboardingStatus;

}
