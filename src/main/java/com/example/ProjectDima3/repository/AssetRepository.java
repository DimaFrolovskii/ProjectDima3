package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    // Query Method 2: Найти все активы конкретного завода
    List<Asset> findByFacilityId(Long facilityId);

    // Query Method 3: Найти активы по типу (например, только "Сервер")
    List<Asset> findByType(String type);
}