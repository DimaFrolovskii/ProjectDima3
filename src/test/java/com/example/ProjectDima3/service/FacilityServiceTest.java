package com.example.ProjectDima3.service;


import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.repository.FacilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private FacilityService facilityService;

    private Facility facility;

    @BeforeEach
    void setUp() {
        facility = new Facility();
        facility.setId(1L);
        facility.setName("Завод Электроника");
    }

    // 1. Позитивный: Получение всех объектов
    @Test
    void shouldReturnAllFacilities_whenCalled() {
        // Arrange
        List<Facility> facilities = Arrays.asList(facility, new Facility());
        when(facilityRepository.findAll()).thenReturn(facilities);

        // Act
        List<Facility> result = facilityService.getAllFacilities();

        // Assert
        assertEquals(2, result.size());
        verify(facilityRepository, times(1)).findAll();
    }

    // 2. Позитивный: Получение по ID
    @Test
    void shouldReturnFacility_whenIdExists() {
        // Arrange
        when(facilityRepository.findById(1L)).thenReturn(Optional.of(facility));

        // Act
        Facility found = facilityService.getFacilityById(1L);

        // Assert
        assertNotNull(found);
        assertEquals("Завод Электроника", found.getName());
    }

    // 3. Негативный: Ошибка, если объект не найден (важно для покрытия)
    @Test
    void shouldThrowException_whenFacilityNotFound() {
        // Arrange
        when(facilityRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> facilityService.getFacilityById(99L));

        assertEquals("Facility not found", exception.getMessage());
    }

    // 4. Позитивный: Создание объекта
    @Test
    void shouldCreateFacility_successfully() {
        // Arrange
        when(facilityRepository.save(any(Facility.class))).thenReturn(facility);

        // Act
        Facility created = facilityService.createFacility(new Facility());

        // Assert
        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(facilityRepository).save(any(Facility.class));
    }

    // 5. Позитивный: Удаление объекта
    @Test
    void shouldDeleteFacility_whenIdProvided() {
        // Arrange
        Long idToDelete = 1L;
        doNothing().when(facilityRepository).deleteById(idToDelete);

        // Act
        facilityService.deleteFacility(idToDelete);

        // Assert
        verify(facilityRepository, times(1)).deleteById(idToDelete);
    }
}