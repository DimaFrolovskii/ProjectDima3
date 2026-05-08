package com.example.ProjectDima3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDto {
    private long userCount;
    private long companyCount;
    private long departmentCount;
    private long assetCount;
    private long incidentCount;
}