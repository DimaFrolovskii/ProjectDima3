package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.entity.Asset;
import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.repository.AssetRepository;
import com.example.ProjectDima3.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final FacilityRepository facilityRepository;

    @Transactional(readOnly = true)
    public Page<AssetDTO> getAllAssets(Pageable pageable) {
        log.info("Получение страницы активов: {}", pageable);
        return assetRepository.findAll(pageable).map(AssetDTO::fromEntity);
    }

    @Transactional
    public AssetDTO createAsset(AssetDTO dto) {
        log.info("Создание нового актива: {}", dto.getName());
        Asset asset = new Asset();

        dto.updateEntity(asset);

        if (dto.getFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getFacilityId())
                    .orElseThrow(() -> new RuntimeException("Объект не найден"));
            asset.setFacility(facility);
        }

        return AssetDTO.fromEntity(assetRepository.save(asset));
    }

    @Transactional
    public AssetDTO updateAsset(Long id, AssetDTO dto) {
        log.info("Обновление актива с ID: {}", id);
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Актив не найден"));
        dto.updateEntity(asset);

        return AssetDTO.fromEntity(assetRepository.save(asset));
    }

    @Transactional
    public void deleteAsset(Long id) {
        log.error("Удаление актива с ID: {}", id);
        assetRepository.deleteById(id);
    }
}