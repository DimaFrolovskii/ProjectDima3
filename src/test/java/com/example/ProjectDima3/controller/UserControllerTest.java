package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.UserAssignmentDto;
import com.example.ProjectDima3.entity.User;
import com.example.ProjectDima3.service.UserService;
import com.example.ProjectDima3.security.JwtUtil; // Импортируем JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService; // Импортируем UserDetailsService
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.ProjectDima3.config.SecurityConfig;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class) // Импортируем конфигурацию безопасности для тестов
class UserControllerTest {

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
    private UserService userService;

    @MockitoBean // Добавляем мок для JwtUtil
    private JwtUtil jwtUtil;

    @MockitoBean // Добавляем мок для UserDetailsService
    private UserDetailsService userDetailsService;

    // Требование 5.1: Позитивный сценарий (200) и проверка JSON
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsers_whenAdmin() throws Exception {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        List<User> users = Collections.singletonList(user);
        when(userService.getAllUsers()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    // Требование 6: Тестирование безопасности (доступ запрещен)
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbidden_whenGetAllUsersAsUser() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    // Требование 5.1: Позитивный сценарий (200)
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAssignUser_whenAdminAndValidData() throws Exception {
        // Arrange
        Long userId = 1L;
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(10L);
        assignmentDto.setDepartmentId(20L);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("assignedUser");

        when(userService.assignUser(eq(userId), any(UserAssignmentDto.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}/assign", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("assignedUser"));
    }

    // Требование 5.2: Негативный сценарий (404)
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFound_whenAssigningNonExistentUser() throws Exception {
        // Arrange
        Long userId = 99L;
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(10L);
        assignmentDto.setDepartmentId(20L);

        when(userService.assignUser(eq(userId), any(UserAssignmentDto.class)))
                .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}/assign", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentDto)))
                .andExpect(status().isNotFound()); // Предполагаем, что у вас есть @ControllerAdvice для обработки RuntimeException в 404
    }

    // Требование 6: Тестирование безопасности (доступ запрещен)
    @Test
    @WithMockUser(roles = "USER") // Роль USER, а метод требует ADMIN
    void shouldReturnForbidden_whenAssignUserAsUser() throws Exception {
        // Arrange
        Long userId = 1L;
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        // ОБЯЗАТЕЛЬНО: заполняем поля, чтобы пройти валидацию (@NotNull)
        assignmentDto.setCompanyId(1L);
        assignmentDto.setDepartmentId(1L);

        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}/assign", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignmentDto))) // Теперь тут валидный JSON
                .andExpect(status().isForbidden()); // Теперь проверка ролей сработает и вернет 403
    }

    // Требование 5.2: Негативный сценарий (400)
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnBadRequest_whenAssignUserWithInvalidData() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/users/{userId}/assign", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Пустое тело, которое должно провалить валидацию
                .andExpect(status().isBadRequest());
    }
}
