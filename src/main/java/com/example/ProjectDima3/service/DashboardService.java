package com.example.ProjectDima3.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("users", 100);
        data.put("companies", 10);
        data.put("departments", 50);
        data.put("assets", 1000);
        return data;
    }
}