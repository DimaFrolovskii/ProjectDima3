package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Incident;
import com.example.ProjectDima3.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;

    public Page<Incident> getIncidentsByAsset(Long assetId, Pageable pageable) {
        log.info("Запрос инцидентов для актива {} с пагинацией", assetId);
        return incidentRepository.findByAssetId(assetId, pageable);
    }

    public Incident reportIncident(Incident incident) {
        log.warn("РЕГИСТРАЦИЯ ИНЦИДЕНТА: {}", incident.getDescription());
        return incidentRepository.save(incident);
    }
}