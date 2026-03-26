package com.merce.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
@ToString(exclude = "hashedPassword")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Email(message = "Invalid Email") @NotBlank(message = "Email is required")
    @Column(name = "email", nullable = false, unique = true)
    String email;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    String name;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Column(name = "hashed_password", nullable = false)
    String hashedPassword;

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "Role is required")
    @Column(name = "role", nullable = false)
    Role role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    Instant createdAt;

    public static User create(String email, String name, String hashedPassword, Role role) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.hashedPassword = hashedPassword;
        user.role = role;
        return user;
    }
}
