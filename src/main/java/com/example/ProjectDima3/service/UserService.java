package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.UserAssignmentDto;
import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.entity.Department;
import com.example.ProjectDima3.entity.User;
import com.example.ProjectDima3.repository.CompanyRepository;
import com.example.ProjectDima3.repository.DepartmentRepository;
import com.example.ProjectDima3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User assignUser(Long userId, UserAssignmentDto assignmentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findById(assignmentDto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Department department = departmentRepository.findById(assignmentDto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (!department.getCompany().getId().equals(company.getId())) {
            throw new IllegalArgumentException("Department does not belong to the specified company");
        }

        user.setCompany(company);
        user.setDepartment(department);

        return userRepository.save(user);
    }
}