package bienew.blog.backend.auth.entity;

import bienew.blog.backend.auth.domain.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false)
    private Long id;

    @Column(name = "google_sub", nullable = false, unique = true)
    private String googleSub;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    public static User create(
            Long id,
            String googleSub,
            String email,
        String name,
        UserRole role
    ) {
        User user = new User();
        user.id = id;
        user.googleSub = googleSub;
        user.updateProfile(email, name, role);
        user.createAt = LocalDateTime.now();
        return user;
    }

    public void updateProfile(String email, String name, UserRole role) {
        this.email = email;
        this.name = name;
        this.role = role;
        this.updateAt = LocalDateTime.now();
    }
}
