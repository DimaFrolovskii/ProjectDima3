package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Asset;
import com.example.ProjectDima3.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;

    public List<Asset> getAssetsByFacility(Long facilityId) {
        log.info("Запрос активов для объекта с id: {}", facilityId);
        return assetRepository.findByFacilityId(facilityId); //Query Method
    }

    public Asset createAsset(Asset asset) {
        log.info("Добавление нового актива: {}", asset.getName());
        return assetRepository.save(asset);
    }
}