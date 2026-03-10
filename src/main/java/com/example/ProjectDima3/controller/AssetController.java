package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @GetMapping("/facility/{facilityId}")
    public ResponseEntity<List<AssetDTO>> getByFacility(@PathVariable Long facilityId) {
        return ResponseEntity.ok(assetService.getAssetsByFacility(facilityId));
    }

    @PostMapping
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO assetDto) {
        return new ResponseEntity<>(assetService.createAsset(assetDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> update(@PathVariable Long id, @RequestBody AssetDTO dto) {
        // В сервисе нужно будет реализовать метод updateAsset
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }
}