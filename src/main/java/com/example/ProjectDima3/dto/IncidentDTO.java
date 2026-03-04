package com.example.ProjectDima3.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentDTO {
    private Long id;
    private String title;
    private String description;
    private String priority; // Low, Medium, High, Critical
    private String status;   // Open, In Progress, Resolved, Closed
    private Long assetId;    // Передаем только ID актива, а не весь объект
    private LocalDateTime reportedAt;
}