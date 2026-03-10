package com.example.ProjectDima3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime; // Не забудьте импорт
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    // ДОБАВЬТЕ ЭТИ ПОЛЯ:
    private String status;
    private String serialNumber;
    private String description;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<Incident> incidents;

    // Автоматическая установка даты создания
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}