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
    private final FacilityRepository facilityRepository; //ManyToOne

    //Логирование
    @Transactional(readOnly = true)
    public Page<AssetDTO> getAllAssets(Pageable pageable) {
        log.info("Получение страницы активов: {}", pageable);
        return assetRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    public AssetDTO createAsset(AssetDTO dto) {
        log.info("Создание нового актива: {}", dto.getName());
        Asset asset = new Asset();
        mapDtoToEntity(dto, asset);
        if (dto.getFacilityId() != null) {
            Facility facility = facilityRepository.findById(dto.getFacilityId())
                    .orElseThrow(() -> new RuntimeException("Объект не найден"));
            asset.setFacility(facility);
        }
        return convertToDTO(assetRepository.save(asset));
    }

    @Transactional
    public AssetDTO updateAsset(Long id, AssetDTO dto) {
        log.info("Обновление актива с ID: {}", id);
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Актив не найден"));
        mapDtoToEntity(dto, asset);
        return convertToDTO(assetRepository.save(asset));
    }

    @Transactional
    public void deleteAsset(Long id) {
        log.error("Удаление актива с ID: {}", id);
        assetRepository.deleteById(id);
    }

    // Вспомогательный маппер
    private AssetDTO convertToDTO(Asset asset) {
        AssetDTO dto = new AssetDTO();
        dto.setId(asset.getId());
        dto.setName(asset.getName());
        dto.setType(asset.getType());
        dto.setStatus(asset.getStatus());
        dto.setSerialNumber(asset.getSerialNumber());
        dto.setDescription(asset.getDescription());
        if (asset.getFacility() != null) {
            dto.setFacilityId(asset.getFacility().getId());
        }
        return dto;
    }

    private void mapDtoToEntity(AssetDTO dto, Asset asset) {
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setStatus(dto.getStatus());
        asset.setSerialNumber(dto.getSerialNumber());
        asset.setDescription(dto.getDescription());
    }
}