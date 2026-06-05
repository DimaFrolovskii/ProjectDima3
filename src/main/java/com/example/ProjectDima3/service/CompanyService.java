package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.CompanyDto;
import com.example.ProjectDima3.dto.CompanyRequest;
import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CompanyDto getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public CompanyDto createCompany(CompanyRequest request) {
        Company company = new Company();
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        return toDto(companyRepository.save(company));
    }

    public CompanyDto updateCompany(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        return toDto(companyRepository.save(company));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    private CompanyDto toDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        return dto;
    }
}
