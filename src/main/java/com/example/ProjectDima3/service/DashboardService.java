package com.example.ProjectDima3.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    public Map<String, Object> getDashboardData() {
        // Здесь будет логика сбора данных для Dashboard
        Map<String, Object> data = new HashMap<>();
        data.put("users", 100); // Пример
        data.put("companies", 10); // Пример
        data.put("departments", 50); // Пример
        data.put("assets", 1000); // Пример
        return data;
    }
}