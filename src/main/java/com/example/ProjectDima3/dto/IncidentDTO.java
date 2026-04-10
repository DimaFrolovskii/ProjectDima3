package com.example.ProjectDima3.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentDTO {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private Long assetId;
    private LocalDateTime reportedAt;
}