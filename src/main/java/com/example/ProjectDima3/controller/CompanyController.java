package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.CompanyDto;
import com.example.ProjectDima3.dto.CompanyRequest;
import com.example.ProjectDima3.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "API для управления компаниями")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "Получить все компании")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAll() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @Operation(summary = "Получить компанию по ID")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @Operation(summary = "Создать компанию")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CompanyRequest request) {
        CompanyDto createdCompany = companyService.createCompany(request);
        return new ResponseEntity<>(createdCompany, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить компанию")
    @SecurityRequirement(name = "bearer-jwt")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyDto> update(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(id, request));
    }

    @Operation(summary = "Удалить компанию")
    @SecurityRequirement(name = "bearer-jwt")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
