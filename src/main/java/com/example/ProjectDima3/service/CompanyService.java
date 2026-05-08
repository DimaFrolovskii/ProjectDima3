package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, Company companyDetails) {
        Company company = getCompanyById(id);
        company.setName(companyDetails.getName());
        company.setAddress(companyDetails.getAddress());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}