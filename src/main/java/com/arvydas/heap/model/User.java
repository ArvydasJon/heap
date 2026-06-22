package com.arvydas.heap.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Loginas – email, unikalus
    @Column(nullable = false, unique = true)
    private String email;

    // Slaptažodis (hashed vėliau)
    @Column(nullable = false)
    private String password;

    // Display vardas, nebūtinai unikalus
    @Column(nullable = false)
    private String username;

    // Vartotojo rolė
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Kada sukurtas vartotojas
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Ryšys su WorkEntry – vienas user turi daug įrašų
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkEntry> workEntries;
}