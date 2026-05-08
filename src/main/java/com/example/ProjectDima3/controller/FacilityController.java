package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Facilities", description = "API для управления объектами")
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(summary = "Получить все объекты")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping
    public ResponseEntity<List<Facility>> getAll() {
        log.info("REST запрос на получение всех объектов");
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @Operation(summary = "Получить объект по ID")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping("/{id}")
    public ResponseEntity<Facility> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getFacilityById(id));
    }

    @Operation(summary = "Создать объект")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping
    public ResponseEntity<Facility> create(@Valid @RequestBody Facility facility) {
        log.info("REST запрос на создание объекта: {}", facility.getName());
        Facility saved = facilityService.createFacility(facility);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Удалить объект")
    @SecurityRequirement(name = "bearer-jwt")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("REST запрос на удаление объекта id: {}", id);
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

}