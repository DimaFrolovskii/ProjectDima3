package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    //Pagination
    @GetMapping
    public ResponseEntity<Page<AssetDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(assetService.getAllAssets(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO assetDto) {
        return new ResponseEntity<>(assetService.createAsset(assetDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> update(@PathVariable Long id, @Valid @RequestBody AssetDTO dto) {
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}