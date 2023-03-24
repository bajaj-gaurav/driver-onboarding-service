package com.example.driveronboardingservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "roles", indexes = {@Index(name = "role_name_index", columnList = "name", unique = true)})
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

}
