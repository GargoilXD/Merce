package com.merce.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Entity(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    final UUID id;
    @Email @Column(name = "email", nullable = false, unique = true)
    final String email;
    @Column(name = "name", nullable = false)
    final String name;
    @Column(name = "hashed_password", nullable = false)
    final String hashed_password;
    @Column(name = "created_at", nullable = false, updatable = false)
    final Instant created_at;
}
