package bienew.blog.backend.auth.service;

import bienew.blog.backend.auth.entity.User;

public record AuthSession(String accessToken, User user) {
}
