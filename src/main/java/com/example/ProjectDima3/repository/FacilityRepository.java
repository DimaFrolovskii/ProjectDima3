package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    // Query Method 1: Поиск объекта по точному имени
    List<Facility> findByNameContainingIgnoreCase(String name);
}