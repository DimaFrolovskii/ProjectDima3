package com.example.ProjectDima3.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssetRequest {
    @NotBlank(message = "Название обязательно")
    private String name;
    @NotBlank(message = "Тип обязателен")
    private String type;
    private String serialNumber;
    private String description;
    private Long facilityId;
}