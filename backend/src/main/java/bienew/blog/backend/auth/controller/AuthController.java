package bienew.blog.backend.auth.controller;

import bienew.blog.backend.auth.dto.AuthResponse;
import bienew.blog.backend.auth.dto.AuthUserResponse;
import bienew.blog.backend.auth.dto.GoogleLoginRequest;
import bienew.blog.backend.auth.service.AuthService;
import bienew.blog.backend.auth.service.AuthSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public AuthResponse loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        AuthSession session = authService.loginWithGoogle(request.credential());
        return new AuthResponse(
                session.accessToken(),
                "Bearer",
                authService.getAccessTokenLifetimeSeconds(),
                AuthUserResponse.from(session.user())
        );
    }
}
