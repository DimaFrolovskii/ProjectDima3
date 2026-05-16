package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.config.SecurityConfig;
import com.example.ProjectDima3.dto.AssetDTO;
import com.example.ProjectDima3.exception.GlobalExceptionHandler;
import com.example.ProjectDima3.exception.ResourceNotFoundException;
import com.example.ProjectDima3.security.JwtUtil; // Импортируем JwtUtil
import com.example.ProjectDima3.service.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService; // Импортируем UserDetailsService
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssetController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})

class AssetControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssetService assetService;

    @MockitoBean // Добавляем мок для JwtUtil
    private JwtUtil jwtUtil;

    @MockitoBean // Добавляем мок для UserDetailsService
    private UserDetailsService userDetailsService;

    // Требование 5.1: Позитивный сценарий (200)
    @Test
    @WithMockUser(roles = "USER")
    void shouldGetAllAssets_whenUser() throws Exception {
        // Arrange
        Page<AssetDTO> assetPage = new PageImpl<>(Collections.singletonList(new AssetDTO()));
        when(assetService.getAllAssets(any(PageRequest.class))).thenReturn(assetPage);

        // Act & Assert
        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // Требование 5.1: Позитивный сценарий (200) и проверка JSON
    @Test
    @WithMockUser(roles = "USER")
    void shouldGetAssetById_whenExists() throws Exception {
        // Arrange
        Long assetId = 1L;
        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setId(assetId);
        assetDTO.setName("Test Asset");
        when(assetService.getAssetById(assetId)).thenReturn(assetDTO);

        // Act & Assert
        mockMvc.perform(get("/api/assets/{id}", assetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assetId))
                .andExpect(jsonPath("$.name").value("Test Asset"));
    }

    // Требование 5.2: Негативный сценарий (404)
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnNotFound_whenGetAssetByIdNotExists() throws Exception {
        // Arrange
        Long assetId = 99L;
        when(assetService.getAssetById(assetId)).thenThrow(new ResourceNotFoundException("Актив не найден"));

        // Act & Assert
        mockMvc.perform(get("/api/assets/{id}", assetId))
                .andDo(print()) // Посмотрите в консоль IDE после запуска!
                .andExpect(status().isNotFound());
    }

    // Требование 5.1: Позитивный сценарий (201)
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateAsset_whenValidData() throws Exception {
        // Arrange
        AssetDTO assetToCreate = new AssetDTO();
        assetToCreate.setName("New Asset");

        AssetDTO createdAsset = new AssetDTO();
        createdAsset.setId(1L);
        createdAsset.setName("New Asset");

        when(assetService.createAsset(any(AssetDTO.class))).thenReturn(createdAsset);

        // Act & Assert
        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Asset"));
    }

    // Требование 5.2: Негативный сценарий (400)
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequest_whenCreateAssetWithInvalidData() throws Exception {
        // Arrange
        AssetDTO invalidAsset = new AssetDTO(); // Имя пустое, что должно быть невалидно
        invalidAsset.setName("");

        // Act & Assert
        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidAsset)))
                .andExpect(status().isBadRequest());
    }

    // Требование 5.1: Позитивный сценарий (204)
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteAsset_whenAdmin() throws Exception {
        // Arrange
        Long assetId = 1L;
        doNothing().when(assetService).deleteAsset(assetId);

        // Act & Assert
        mockMvc.perform(delete("/api/assets/{id}", assetId))
                .andExpect(status().isNoContent());
    }

    // Требование 6: Тестирование безопасности (доступ запрещен)
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenDeleteAssetAsUser() throws Exception {
        // Arrange
        Long assetId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/assets/{id}", assetId))
                .andExpect(status().isForbidden());
    }
}
