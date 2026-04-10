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
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<Facility> getAllFacilities() {
        log.info("Запрос списка всех объектов КИИ");
        return facilityRepository.findAll();
    }

    @Transactional
    public Facility createFacility(Facility facility) {
        log.debug("Сохранение нового объекта: {}", facility.getName());
        return facilityRepository.save(facility);
    }

    public void deleteFacility(Long id) {
        log.warn("Удаление объекта с id: {}", id);
        facilityRepository.deleteById(id);
    }
}