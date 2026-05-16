package com.example.ProjectDima3.service;

import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.repository.CompanyRepository;
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
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Главная Компания");
        company.setAddress("ул. Пушкина, д. 10");
    }

    @Test
    void shouldReturnAllCompanies() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(Arrays.asList(company));

        // Act
        List<Company> result = companyService.getAllCompanies();

        // Assert
        assertEquals(1, result.size());
        verify(companyRepository).findAll();
    }

    @Test
    void shouldReturnCompany_whenIdExists() {
        // Arrange
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        // Act
        Company found = companyService.getCompanyById(1L);

        // Assert
        assertNotNull(found);
        assertEquals("Главная Компания", found.getName());
    }

    @Test
    void shouldThrowException_whenCompanyNotFound() {
        // Arrange
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> companyService.getCompanyById(99L));

        assertEquals("Company not found", exception.getMessage());
    }

    @Test
    void shouldCreateCompany() {
        // Arrange
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Act
        Company created = companyService.createCompany(new Company());

        // Assert
        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    void shouldUpdateCompany_whenValidData() {
        // Arrange
        Company updatedDetails = new Company();
        updatedDetails.setName("Новое Имя");
        updatedDetails.setAddress("Новый Адрес");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Company result = companyService.updateCompany(1L, updatedDetails);

        // Assert
        assertEquals("Новое Имя", result.getName());
        assertEquals("Новый Адрес", result.getAddress());
        verify(companyRepository).save(company);
    }

    @Test
    void shouldDeleteCompany() {
        // Arrange
        doNothing().when(companyRepository).deleteById(1L);

        // Act
        companyService.deleteCompany(1L);

        // Assert
        verify(companyRepository, times(1)).deleteById(1L);
    }
}