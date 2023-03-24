package com.example.driveronboardingservice.repository;

import com.example.driveronboardingservice.entity.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long> {
    DocumentCategory findByName(String name);
}
