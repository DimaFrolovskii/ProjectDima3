package com.example.ProjectDima3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Название завода
    private String address;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    private List<Asset> assets;
}