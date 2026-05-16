package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.entity.Asset;
import com.example.ProjectDima3.entity.Facility;
import com.example.ProjectDima3.repository.AssetRepository;
import com.example.ProjectDima3.repository.FacilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private AssetService assetService;

    private Asset asset;
    private AssetDTO assetDTO;

    @BeforeEach
    void setUp() {
        asset = new Asset();
        asset.setId(1L);
        asset.setName("Test Asset");

        assetDTO = new AssetDTO();
        assetDTO.setName("Test Asset");
    }

    // Требование 2.1: Позитивный сценарий
    @Test
    void shouldCreateAsset_whenValidData() {
        // Arrange
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);

        // Act
        AssetDTO saved = assetService.createAsset(assetDTO);

        // Assert
        assertNotNull(saved);
        assertEquals("Test Asset", saved.getName());
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenUpdateAssetNotFound() {
        // Arrange
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> assetService.updateAsset(1L, assetDTO));
        assertEquals("Актив не найден", exception.getMessage());    }

    // Требование 2.4: Проверка взаимодействий (verify)
    @Test
    void shouldDeleteAsset_whenValidId() {
        // Arrange
        // Убрали when(assetRepository.existsById(1L)).thenReturn(true); так как он не вызывается
        doNothing().when(assetRepository).deleteById(1L);

        // Act
        assetService.deleteAsset(1L);

        // Assert
        verify(assetRepository, times(1)).deleteById(1L);
    }
    
    // Требование 2.4: Проверка взаимодействий (verify) - негативный сценарий
    @Test
    void shouldThrowExceptionOnDelete_whenAssetNotFound() {
        // Arrange
        doThrow(new RuntimeException("Актив с ID 99 не найден")).when(assetRepository).deleteById(99L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> assetService.deleteAsset(99L));
        assertEquals("Актив с ID 99 не найден", exception.getMessage());
        verify(assetRepository, times(1)).deleteById(99L);
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenGetAssetNotFound() {
        // Arrange
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> assetService.getAssetById(99L));
        assertEquals("Актив не найден", exception.getMessage());    }

    // Требование 2.1: Позитивный сценарий
    @Test
    void shouldUpdateAsset_whenValidData() {
        // Arrange
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        AssetDTO updatedDto = new AssetDTO();
        updatedDto.setName("Updated Asset");

        // Act
        AssetDTO updated = assetService.updateAsset(1L, updatedDto);

        // Assert
        assertNotNull(updated);
        assertEquals("Updated Asset", updated.getName());
        verify(assetRepository).save(any(Asset.class));
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenFacilityNotFoundOnCreate() {
        // Arrange
        assetDTO.setFacilityId(1L);
        when(facilityRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> assetService.createAsset(assetDTO));
        assertEquals("Объект не найден", exception.getMessage());   }

    // Требование 3: Параметризованный тест
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 99L})
    void shouldFindAssetById_forDifferentIds(Long assetId) {
        // Arrange
        Asset tempAsset = new Asset();
        tempAsset.setId(assetId);
        tempAsset.setName("Some Asset");
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(tempAsset));

        // Act
        AssetDTO found = assetService.getAssetById(assetId);

        // Assert
        assertNotNull(found);
        assertEquals(assetId, found.getId());
    }
    
    // Требование 3: Второй параметризованный тест
    @ParameterizedTest
    @CsvSource({"New Asset, 1", "Another Asset, 2"})
    void shouldCreateAssetWithDifferentNamesAndFacilities(String name, Long facilityId) {
        // Arrange
        AssetDTO dto = new AssetDTO();
        dto.setName(name);
        dto.setFacilityId(facilityId);

        Facility facility = new Facility();
        facility.setId(facilityId);

        Asset savedAsset = new Asset();
        savedAsset.setName(name);
        savedAsset.setFacility(facility);

        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));
        when(assetRepository.save(any(Asset.class))).thenReturn(savedAsset);

        // Act
        AssetDTO created = assetService.createAsset(dto);

        // Assert
        assertEquals(name, created.getName());
        assertEquals(facilityId, created.getFacilityId());
    }

    // Требование 2.1: Позитивный сценарий
    @Test
    void shouldGetAssetById_whenExists() {
        // Arrange
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        
        // Act
        AssetDTO found = assetService.getAssetById(1L);
        
        // Assert
        assertNotNull(found);
        assertEquals(asset.getId(), found.getId());
    }

    // Требование 2.1: Позитивный сценарий
    @Test
    void shouldGetAllAssets_whenCalled() {
        // Arrange
        Page<Asset> page = new PageImpl<>(Collections.singletonList(asset));
        when(assetRepository.findAll(any(PageRequest.class))).thenReturn(page);
        
        // Act
        Page<AssetDTO> result = assetService.getAllAssets(PageRequest.of(0, 10));
        
        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    // Требование 2.5: Тест с использованием ArgumentCaptor
    @Test
    void shouldCaptureAssetArgument_whenCreatingAsset() {
        // Arrange
        ArgumentCaptor<Asset> assetCaptor = ArgumentCaptor.forClass(Asset.class);
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        
        // Act
        assetService.createAsset(assetDTO);
        
        // Assert
        verify(assetRepository).save(assetCaptor.capture());
        Asset capturedAsset = assetCaptor.getValue();
        assertEquals(assetDTO.getName(), capturedAsset.getName());
    }

}
