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

    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAll() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @PostMapping
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO assetDto) {
        return new ResponseEntity<>(assetService.createAsset(assetDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> update(@PathVariable Long id, @RequestBody AssetDTO dto) {
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.deleteAsset(id); // Реализуй удаление в сервисе
        return ResponseEntity.noContent().build();
    }
}