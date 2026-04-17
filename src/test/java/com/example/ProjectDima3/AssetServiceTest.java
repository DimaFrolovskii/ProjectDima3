package com.example.ProjectDima3;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.entity.Asset;
import com.example.ProjectDima3.repository.AssetRepository;
import com.example.ProjectDima3.repository.FacilityRepository;
import com.example.ProjectDima3.service.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void testCreateAsset_Success() {
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        AssetDTO saved = assetService.createAsset(assetDTO);
        assertNotNull(saved);
        assertEquals("Test Asset", saved.getName());
    }

    @Test
    void testUpdateAsset_NotFound() {
        when(assetRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> assetService.updateAsset(1L, assetDTO));
    }

    @Test
    void testDeleteAsset_Success() {
        doNothing().when(assetRepository).deleteById(1L);
        assetService.deleteAsset(1L);
        verify(assetRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAsset_NotFound() {
        when(assetRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> assetService.updateAsset(99L, assetDTO));
    }

    @Test
    void testUpdateAsset_Success() {
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        AssetDTO updated = assetService.updateAsset(1L, assetDTO);
        assertNotNull(updated);
        verify(assetRepository).save(any(Asset.class));
    }
}