package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.UserAssignmentDto;
import com.example.ProjectDima3.entity.User;
import com.example.ProjectDima3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Импорт для валидации
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API для управления пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить всех пользователей")
    @SecurityRequirement(name = "bearer-jwt")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Назначить компанию и отдел пользователю")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping("/{userId}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    // ДОБАВЛЕНО: @Valid перед @RequestBody
    public ResponseEntity<User> assignUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserAssignmentDto assignmentDto) {

        User updatedUser = userService.assignUser(userId, assignmentDto);
        return ResponseEntity.ok(updatedUser);
    }
}