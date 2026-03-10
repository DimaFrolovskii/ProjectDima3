package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    //Query Method 2: Найти все активы конкретного завода
    List<Asset> findByFacilityId(Long facilityId);

    //Query Method 3: Найти активы по типу (например, только "Сервер")
    List<Asset> findByType(String type);

    //поиск по части имени и статусу одновременно
    @Query("SELECT a FROM Asset a WHERE a.name LIKE %:name% AND a.status = :status")
    List<Asset> findByNameAndStatus(@Param("name") String name, @Param("status") String status);
}