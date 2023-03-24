package com.example.driveronboardingservice.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "document_upload", indexes = {
        @Index(name = "file_hash_index", columnList = "file_hash", unique = true),
        @Index(name = "document_url_index", columnList = "location_url", unique = true),
        @Index(name = "driver_doc_index", columnList = "driver_id, document_category_id", unique = true),
})
@Data
public class DocumentUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_hash", nullable = false, unique = true)
    private String fileHash;

    @Column(name = "location_url", nullable = false)
    private String locationUrl;

    @ManyToOne
    @JoinColumn(name = "document_category_id")
    private DocumentCategory documentCategory;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverProfile driverProfile;

    @Column(name = "document_upload_date_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp documentUploadDateTime;


}

