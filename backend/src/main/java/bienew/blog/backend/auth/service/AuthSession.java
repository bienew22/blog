package bienew.blog.backend.auth.service;

import bienew.blog.backend.auth.entity.User;

public record AuthSession(String accessToken, String refreshToken, User user) {

    public static AuthSession of(String accessToken, String refreshToken, User user) {
        return new AuthSession(accessToken, refreshToken, user);
    }
}
