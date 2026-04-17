package com.example.ProjectDima3.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime; // Не забудьте импорт
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor

@Data
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 2, max = 100)
    private String name;
    @NotBlank(message = "Тип обязателен")
    private String type;
    private String status;
    @Column(unique = true)
    private String serialNumber;
    private String description;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY) //по фен-шую
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<Incident> incidents;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}