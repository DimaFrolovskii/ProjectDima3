package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor // Автоматический конструктор
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<Facility> getAllFacilities() {
        log.info("Запрос списка всех объектов КИИ"); // INFO лог по п. 8
        return facilityRepository.findAll();
    }

    @Transactional
    public Facility createFacility(Facility facility) {
        log.debug("Сохранение нового объекта: {}", facility.getName()); // DEBUG лог
        return facilityRepository.save(facility);
    }

    public void deleteFacility(Long id) {
        log.warn("Удаление объекта с id: {}", id); // Логирование важных операций
        facilityRepository.deleteById(id);
    }
}