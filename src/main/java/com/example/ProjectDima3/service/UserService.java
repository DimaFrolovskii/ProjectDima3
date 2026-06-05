package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.UserAssignmentDto;
import com.example.ProjectDima3.dto.UserResponse;
import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.entity.Department;
import com.example.ProjectDima3.entity.User;
import com.example.ProjectDima3.repository.CompanyRepository;
import com.example.ProjectDima3.repository.DepartmentRepository;
import com.example.ProjectDima3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public UserResponse assignUser(Long userId, UserAssignmentDto assignmentDto) {
        User userToAssign = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findById(assignmentDto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Department department = departmentRepository.findById(assignmentDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (currentUser.getRole().equals("ROLE_MANAGER")) {
            if (currentUser.getCompany() == null || !currentUser.getCompany().getId().equals(company.getId())) {
                throw new SecurityException("Manager can only assign users to their own company.");
            }
        }

        if (!department.getCompany().getId().equals(company.getId())) {
            throw new IllegalArgumentException("Department does not belong to the specified company");
        }

        userToAssign.setCompany(company);
        userToAssign.setDepartment(department);

        User updatedUser = userRepository.save(userToAssign);
        return toUserResponse(updatedUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!newRole.equals("ROLE_USER") && !newRole.equals("ROLE_MANAGER") && !newRole.equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Invalid role specified: " + newRole);
        }

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
