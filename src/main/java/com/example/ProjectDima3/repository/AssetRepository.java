package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByFacilityId(Long facilityId);

    List<Asset> findByType(String type);

    @Query("SELECT a FROM Asset a WHERE a.name LIKE %:name% AND a.status = :status")
    List<Asset> findByNameAndStatus(@Param("name") String name, @Param("status") String status);

    // Новый метод для поиска активов по ID компании с пагинацией
    Page<Asset> findByCompanyId(Long companyId, Pageable pageable);
}
