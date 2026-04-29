package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Validated
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @Operation(summary = "Получить всех активов")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<AssetDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(assetService.getAllAssets(PageRequest.of(page, size)));
    }

    @Operation(summary = "создать актив")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO assetDto) {
        return new ResponseEntity<>(assetService.createAsset(assetDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить актив")
    @SecurityRequirement(name = "bearer-jwt")

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AssetDTO> update(@PathVariable Long id, @Valid @RequestBody AssetDTO dto) {
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @Operation(summary = "Удалить актив")
    @SecurityRequirement(name = "bearer-jwt")
    //админ
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}