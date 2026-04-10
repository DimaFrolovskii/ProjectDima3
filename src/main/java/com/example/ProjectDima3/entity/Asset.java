package com.example.ProjectDima3.entity;

import jakarta.persistence.*;
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
    private String name;
    private String type;
    private String status;
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