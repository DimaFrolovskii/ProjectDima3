package com.example.ProjectDima3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
    @Entity
    @Data
    @NoArgsConstructor
    public class Incident {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String description;
        private LocalDateTime createdAt = LocalDateTime.now();

        @ManyToOne
        @JoinColumn(name = "asset_id")
        private Asset asset;
    }
