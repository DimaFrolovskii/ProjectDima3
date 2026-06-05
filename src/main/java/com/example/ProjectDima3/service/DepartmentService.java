package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.DepartmentDto;
import com.example.ProjectDima3.dto.DepartmentRequest;
import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.entity.Department;
import com.example.ProjectDima3.repository.CompanyRepository;
import com.example.ProjectDima3.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public List<DepartmentDto> getDepartmentsByCompanyId(Long companyId) {
        return departmentRepository.findByCompanyId(companyId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto createDepartment(DepartmentRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        Department department = new Department();
        department.setName(request.getName());
        department.setCompany(company);
        return toDto(departmentRepository.save(department));
    }

    public DepartmentDto updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        department.setName(request.getName());
        department.setCompany(company);
        return toDto(departmentRepository.save(department));
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    private DepartmentDto toDto(Department department) {
        DepartmentDto dto = new DepartmentDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        if (department.getCompany() != null) {
            dto.setCompanyId(department.getCompany().getId());
        }
        return dto;
    }
}
