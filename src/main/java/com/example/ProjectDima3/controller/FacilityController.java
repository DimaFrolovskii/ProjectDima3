package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.service.FacilityService;
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
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping
    public ResponseEntity<List<Facility>> getAll() {
        log.info("REST запрос на получение всех объектов");
        return ResponseEntity.ok(facilityService.getAllFacilities());
    }

    @PostMapping
    public ResponseEntity<Facility> create(@Valid @RequestBody Facility facility) {
        log.info("REST запрос на создание объекта: {}", facility.getName());
        Facility saved = facilityService.createFacility(facility);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("REST запрос на удаление объекта id: {}", id);
        facilityService.deleteFacility(id);
        return ResponseEntity.noContent().build();
    }

}