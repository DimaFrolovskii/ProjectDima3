package com.example.ProjectDima3.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRoleUpdateDto {
    @NotBlank(message = "Новая роль не может быть пустой")
    private String newRole;
}