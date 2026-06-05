package com.example.ProjectDima3.controller;

import com.example.ProjectDima3.dto.UserAssignmentDto;
import com.example.ProjectDima3.dto.UserResponse;
import com.example.ProjectDima3.dto.UserRoleUpdateDto;
import com.example.ProjectDima3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Назначить компанию и отдел пользователю")
    @SecurityRequirement(name = "bearer-jwt")
    @PostMapping("/{userId}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UserResponse> assignUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserAssignmentDto assignmentDto) {

        UserResponse updatedUser = userService.assignUser(userId, assignmentDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Обновить роль пользователя")
    @SecurityRequirement(name = "bearer-jwt")
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UserRoleUpdateDto roleUpdateDto) {

        UserResponse updatedUser = userService.updateUserRole(userId, roleUpdateDto.getNewRole());
        return ResponseEntity.ok(updatedUser);
    }
}
