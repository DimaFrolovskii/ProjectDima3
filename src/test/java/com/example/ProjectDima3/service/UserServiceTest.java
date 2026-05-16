package com.example.ProjectDima3.service;

import com.example.ProjectDima3.dto.UserAssignmentDto;
import com.example.ProjectDima3.entity.Company;
import com.example.ProjectDima3.entity.Department;
import com.example.ProjectDima3.entity.User;
import com.example.ProjectDima3.repository.CompanyRepository;
import com.example.ProjectDima3.repository.DepartmentRepository;
import com.example.ProjectDima3.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private UserService userService;

    // Требование 2.1: Unit-тест (Service слой), позитивный сценарий
    // Требование 2.4: Проверка взаимодействий (verify)
    @Test
    void shouldAssignUser_whenValidData() {
        // Arrange
        Long userId = 1L;
        Long companyId = 10L;
        Long departmentId = 20L;
        User user = new User();
        user.setId(userId);
        Company company = new Company();
        company.setId(companyId);
        Department department = new Department();
        department.setId(departmentId);
        department.setCompany(company);
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(companyId);
        assignmentDto.setDepartmentId(departmentId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.assignUser(userId, assignmentDto);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(companyId, updatedUser.getCompany().getId());
        assertEquals(departmentId, updatedUser.getDepartment().getId());
        verify(userRepository).save(user); // Проверяем, что метод save был вызван
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenUserNotFound() {
        // Arrange
        Long userId = 1L;
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(10L);
        assignmentDto.setDepartmentId(20L);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.assignUser(userId, assignmentDto));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Проверяем, что метод НЕ был вызван
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenCompanyNotFound() {
        // Arrange
        Long userId = 1L;
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(10L);
        assignmentDto.setDepartmentId(20L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(companyRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.assignUser(userId, assignmentDto));
        assertEquals("Company not found", exception.getMessage());
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenDepartmentNotFound() {
        // Arrange
        Long userId = 1L;
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(10L);
        assignmentDto.setDepartmentId(20L);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(new Company()));
        when(departmentRepository.findById(20L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.assignUser(userId, assignmentDto));
        assertEquals("Department not found", exception.getMessage());
    }

    // Требование 2.2 и 4: Негативный сценарий и Тестирование исключений
    @Test
    void shouldThrowException_whenDepartmentDoesNotBelongToCompany() {
        // Arrange
        User user = new User();
        Company company = new Company();
        company.setId(10L);
        Company anotherCompany = new Company();
        anotherCompany.setId(11L);
        Department department = new Department();
        department.setCompany(anotherCompany); // Отдел принадлежит другой компании
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(company.getId());
        assignmentDto.setDepartmentId(20L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(departmentRepository.findById(20L)).thenReturn(Optional.of(department));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.assignUser(1L, assignmentDto));
        assertEquals("Department does not belong to the specified company", exception.getMessage());
    }

    // Требование 3: Параметризованный тест (исправленный)
    @ParameterizedTest
    @CsvSource(value = { "1, 10, null", "1, null, 20" }, nullValues = "null")
    void shouldThrowException_whenAssignUserWithInvalidData(Long userId, Long companyId, Long departmentId) {
        // Arrange
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(companyId);
        assignmentDto.setDepartmentId(departmentId);
        
        // Моки для частичного выполнения, если нужно
        if (userId != null) {
            when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        }
        if (companyId != null) {
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(new Company()));
        }

        // Act & Assert
        // Ожидаем RuntimeException, так как сервис выбрасывает его при отсутствии данных
        assertThrows(RuntimeException.class, () -> userService.assignUser(userId, assignmentDto));
    }

    // Требование 3: Второй параметризованный тест
    @ParameterizedTest
    @CsvSource({ "1, 10, 100", "2, 20, 200", "99, 50, 500" })
    void shouldAssignUserSuccessfully_forDifferentValidInputs(Long userId, Long companyId, Long departmentId) {
        // Arrange
        User user = new User();
        user.setId(userId);
        Company company = new Company();
        company.setId(companyId);
        Department department = new Department();
        department.setId(departmentId);
        department.setCompany(company); // Важно для прохождения проверки
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(companyId);
        assignmentDto.setDepartmentId(departmentId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.assignUser(userId, assignmentDto);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(companyId, updatedUser.getCompany().getId());
        assertEquals(departmentId, updatedUser.getDepartment().getId());
        verify(userRepository).save(user);
    }


    // Требование 2.5: Тест с использованием ArgumentCaptor
    @Test
    void shouldCaptureUserArgument_whenAssigningUser() {
        // Arrange
        Long userId = 1L;
        Long companyId = 10L;
        Long departmentId = 20L;
        User user = new User();
        Company company = new Company();
        company.setId(companyId);
        Department department = new Department();
        department.setId(departmentId);
        department.setCompany(company);
        UserAssignmentDto assignmentDto = new UserAssignmentDto();
        assignmentDto.setCompanyId(companyId);
        assignmentDto.setDepartmentId(departmentId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        userService.assignUser(userId, assignmentDto);

        // Assert
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertEquals(companyId, capturedUser.getCompany().getId());
        assertEquals(departmentId, capturedUser.getDepartment().getId());
    }
}