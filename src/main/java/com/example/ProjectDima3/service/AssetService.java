package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.entity.Asset;
import com.example.ProjectDima3.repository.AssetRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Data

public class AssetService {

    private final AssetRepository assetRepository;

    public List<AssetDTO> getAllAssets() {
        log.info("Запрос всех активов");
        return assetRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AssetDTO> getAssetsByFacility(Long facilityId) {
        log.info("Запрос активов для объекта с id: {}", facilityId);
        return assetRepository.findByFacilityId(facilityId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AssetDTO getAssetById(Long id) {
        log.info("Поиск актива по id: {}", id);
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        return convertToDTO(asset);
    }

    public AssetDTO createAsset(AssetDTO dto) {
        log.info("Добавление нового актива: {}", dto.getName());
        Asset asset = new Asset();
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setStatus(dto.getStatus());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setDescription(dto.getDescription());

        Asset savedAsset = assetRepository.save(asset);
        return convertToDTO(savedAsset);
    }

    public void deleteAsset(Long id) {
        log.info("Удаление актива с id: {}", id);
        assetRepository.deleteById(id);
    }

    // Вспомогательный метод для конвертации (Маппер)
    private AssetDTO convertToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setType(asset.getType());
        dto.setStatus(asset.getStatus());
        dto.setSerialNumber(asset.getSerialNumber());
        dto.setDescription(asset.getDescription());
        dto.setCreatedAt(asset.getCreatedAt());
        return dto;
    }
    public AssetDTO updateAsset(Long id, AssetDTO dto) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setStatus(dto.getStatus());

        Asset updated = assetRepository.save(asset);
        return convertToDTO(updated);
    }
}