package bienew.blog.backend.auth.dto;

import bienew.blog.backend.auth.entity.User;

public record AuthUserResponse(String email, String name, String role) {
    public static AuthUserResponse from(User user) {
        return new AuthUserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}
