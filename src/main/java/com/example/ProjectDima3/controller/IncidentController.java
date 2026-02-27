package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.entity.Incident;
import com.example.ProjectDima3.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;

    // Пример вызова: /api/incidents/asset/1?page=0&size=5
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<Page<Incident>> getByAsset(@PathVariable Long assetId, Pageable pageable) {
        return ResponseEntity.ok(incidentService.getIncidentsByAsset(assetId, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')") // Инциденты пишет дежурный или админ
    public ResponseEntity<Incident> create(@RequestBody Incident incident) {
        return ResponseEntity.status(201).body(incidentService.reportIncident(incident));
    }
}