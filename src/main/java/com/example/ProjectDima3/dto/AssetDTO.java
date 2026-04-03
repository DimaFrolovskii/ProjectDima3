package com.example.ProjectDima3.dto;

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
}