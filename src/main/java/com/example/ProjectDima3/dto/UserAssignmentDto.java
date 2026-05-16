package com.example.ProjectDima3.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserAssignmentDto {
    @NotNull(message = "Company ID is required")
    private Long companyId;
    @NotNull(message = "Department ID is required")
    private Long departmentId;
}