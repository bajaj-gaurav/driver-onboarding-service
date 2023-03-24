package com.example.driveronboardingservice.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "document_category", indexes = {
        @Index(name = "document_category_index", columnList = "name", unique = true)
})
@Data
public class DocumentCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private String description;
}
