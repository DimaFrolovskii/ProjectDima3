package com.example.ProjectDima3.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank(message = "Название компании не может быть пустым")
    private String name;
    private String address;
}
