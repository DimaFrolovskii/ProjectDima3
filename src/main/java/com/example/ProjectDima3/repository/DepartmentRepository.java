package com.example.ProjectDima3.repository;

import com.example.ProjectDima3.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByCompanyId(Long companyId);
}