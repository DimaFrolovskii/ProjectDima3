package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    Page<Incident> findByAssetId(Long assetId, Pageable pageable);
}