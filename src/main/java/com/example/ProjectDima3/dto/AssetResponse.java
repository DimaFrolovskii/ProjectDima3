package com.example.ProjectDima3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AssetResponse {
    private Long id;
    private String name;
    private String type;
    private String status;
    private String serialNumber;
    private LocalDateTime createdAt;
    private String facilityName;
}