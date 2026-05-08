package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.DashboardStatsDto;
import com.example.ProjectDima3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final AssetRepository assetRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public DashboardStatsDto getStats() {
        long companyCount = companyRepository.count();
        long departmentCount = departmentRepository.count();
        long assetCount = assetRepository.count();
        long incidentCount = incidentRepository.count();
        long userCount = userRepository.count();

        return new DashboardStatsDto(userCount, companyCount, departmentCount, assetCount, incidentCount);
    }
}