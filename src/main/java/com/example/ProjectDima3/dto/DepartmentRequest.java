package com.example.ProjectDima3.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartmentRequest {
    @NotBlank(message = "Название отдела не может быть пустым")
    private String name;

    @NotNull(message = "ID компании не может быть пустым")
    private Long companyId;
}
