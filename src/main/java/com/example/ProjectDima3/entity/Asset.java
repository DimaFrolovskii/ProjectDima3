package com.example.ProjectDima3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

    @Entity
    @Data
    @NoArgsConstructor
    public class Asset {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name; // Например, "Сервер БД №1"
        private String type; // Тип: Сервер, Датчик, Камера

        @ManyToOne
        @JoinColumn(name = "facility_id")
        private Facility facility; // Ссылка на завод (матрешка)

        @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
        private List<Incident> incidents;
    }

