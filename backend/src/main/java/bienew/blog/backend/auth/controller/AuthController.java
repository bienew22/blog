package bienew.blog.backend.auth.controller;

import bienew.blog.backend.auth.dto.AuthResponse;
import bienew.blog.backend.auth.dto.AuthUserResponse;
import bienew.blog.backend.auth.dto.GoogleLoginRequest;
import bienew.blog.backend.auth.service.AuthService;
import bienew.blog.backend.auth.service.AuthSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String COOKIE_PATH = "/api/v1/auth";

    private final AuthService authService;

    @Value("${app.auth.cookie-secure:false}")
    private boolean cookieSecure;

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        return respond(authService.loginWithGoogle(request.credential()));
    }

    // refresh 쿠키가 없으면 204, 만료/무효면 쿠키를 제거하고 401
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.noContent().build();
        }
        try {
            return respond(authService.refresh(refreshToken));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie("", Duration.ZERO).toString())
                    .build();
        }
    }

    // access token 유효성 확인용. 무효/만료는 보안 필터에서 401 을 반환한다. 유효인 경우 204 를 반환한다.
    @GetMapping("/validate")
    public ResponseEntity<Void> validate() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_COOKIE, required = false) String refreshToken
    ) {
        authService.logout(refreshToken);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshCookie("", Duration.ZERO).toString())
                .build();
    }

    private ResponseEntity<AuthResponse> respond(AuthSession session) {
        AuthResponse body = new AuthResponse(
                session.accessToken(),
                "Bearer",
                authService.getAccessTokenLifetimeSeconds(),
                AuthUserResponse.from(session.user())
        );
        ResponseCookie cookie = refreshCookie(
                session.refreshToken(),
                Duration.ofSeconds(authService.getRefreshTokenLifetimeSeconds())
        );
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(body);
    }

    private ResponseCookie refreshCookie(String value, Duration maxAge) {
        return ResponseCookie.from(REFRESH_COOKIE, value)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path(COOKIE_PATH)
                .maxAge(maxAge)
                .build();
    }
}
