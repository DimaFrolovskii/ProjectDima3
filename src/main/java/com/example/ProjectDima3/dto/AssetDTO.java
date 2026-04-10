package com.example.ProjectDima3.dto;

import com.example.ProjectDima3.entity.Asset;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AssetDTO {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String serialNumber;
    private String description;
    private LocalDateTime createdAt;
    private Long facilityId;

    public static AssetDTO fromEntity(Asset asset) {
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

    public void updateEntity(Asset asset) {
        asset.setName(this.getName());
        asset.setType(this.getType());
        asset.setStatus(this.getStatus());
        asset.setSerialNumber(this.getSerialNumber());
        asset.setDescription(this.getDescription());
    }
}