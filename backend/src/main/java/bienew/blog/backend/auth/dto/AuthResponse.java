package bienew.blog.backend.auth.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresIn, AuthUserResponse user) {
}
