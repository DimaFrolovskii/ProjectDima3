package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.DepartmentDto;
import com.example.ProjectDima3.dto.DepartmentRequest;
import com.example.ProjectDima3.service.DepartmentService;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "API для управления отделами")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Получить все отделы")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAll() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @Operation(summary = "Получить отдел по ID")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @Operation(summary = "Получить все отделы компании")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<DepartmentDto>> getByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(departmentService.getDepartmentsByCompanyId(companyId));
    }

    @Operation(summary = "Создать отдел")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto> create(@Valid @RequestBody DepartmentRequest request) {
        DepartmentDto createdDepartment = departmentService.createDepartment(request);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить отдел")
    @SecurityRequirement(name = "bearer-jwt")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, request));
    }

    @Operation(summary = "Удалить отдел")
    @SecurityRequirement(name = "bearer-jwt")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
