package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Asset;
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
    public ResponseEntity<List<Asset>> getByFacility(@PathVariable Long facilityId) {
        return ResponseEntity.ok(assetService.getAssetsByFacility(facilityId));
    }

    @PostMapping
    public ResponseEntity<Asset> create(@Valid @RequestBody Asset asset) {
        return new ResponseEntity<>(assetService.createAsset(asset), HttpStatus.CREATED);
    }
}