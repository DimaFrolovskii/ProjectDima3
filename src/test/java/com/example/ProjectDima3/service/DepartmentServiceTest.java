package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.entity.Department;
import com.example.ProjectDima3.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(10L);
        company.setName("Test Company");

        department = new Department();
        department.setId(1L);
        department.setName("IT Department");
        department.setCompany(company);
    }

    @Test
    void shouldReturnAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(department));

        List<Department> result = departmentService.getAllDepartments();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(departmentRepository).findAll();
    }

    @Test
    void shouldReturnDepartment_whenIdExists() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Department found = departmentService.getDepartmentById(1L);

        assertNotNull(found);
        assertEquals("IT Department", found.getName());
    }

    @Test
    void shouldThrowException_whenDepartmentNotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> departmentService.getDepartmentById(99L));

        assertEquals("Department not found", exception.getMessage());
    }

    @Test
    void shouldCreateDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department created = departmentService.createDepartment(new Department());

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(departmentRepository).save(any(Department.class));
    }

    @Test
    void shouldUpdateDepartment_whenValidData() {
        // Arrange
        Department updatedDetails = new Department();
        updatedDetails.setName("New IT Name");
        updatedDetails.setCompany(company);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Department result = departmentService.updateDepartment(1L, updatedDetails);

        // Assert
        assertEquals("New IT Name", result.getName());
        verify(departmentRepository).save(department);
    }

    @Test
    void shouldDeleteDepartment() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnDepartmentsByCompanyId() {
        // Arrange
        Long companyId = 10L;
        when(departmentRepository.findByCompanyId(companyId)).thenReturn(Arrays.asList(department));

        // Act
        List<Department> result = departmentService.getDepartmentsByCompanyId(companyId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(companyId, result.get(0).getCompany().getId());
        verify(departmentRepository).findByCompanyId(companyId);
    }
}