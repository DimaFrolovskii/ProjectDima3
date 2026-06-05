package com.example.ProjectDima3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private Long companyId;
    private String companyName;
    private Long departmentId;
    private String departmentName;
}
