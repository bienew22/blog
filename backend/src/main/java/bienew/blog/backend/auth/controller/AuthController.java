package bienew.blog.backend.auth.controller;

import bienew.blog.backend.auth.dto.AuthResponse;
import bienew.blog.backend.auth.dto.AuthUserResponse;
import bienew.blog.backend.auth.dto.LoginRequest;
import bienew.blog.backend.auth.service.AuthService;
import bienew.blog.backend.auth.service.AuthSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "인증", description = "인증 관련 API")
public class AuthController {

    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String COOKIE_PATH = "/api/v1/auth";

    private final AuthService authService;

    @Value("${app.auth.cookie-secure:false}")
    private boolean cookieSecure;

    @Operation(
            summary = "로그인",
            description = "성공 시 access token과 refresh token이 발급됩니다."
    )
    @PostMapping("/tokens")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return respond(authService.login(request));
    }

    @Operation(
            summary = "토큰 갱신",
            description = "만료된 access token을 refresh token으로 갱신합니다. refresh token이 없거나 무효/만료된 경우 401을 반환합니다."
    )
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

    @Operation(
            summary = "액세스 토큰 유효성 확인",
            description = "액세스 토큰의 유효성을 확인합니다. 무효/만료된 경우 401을 반환하고, 유효한 경우 200을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    public ResponseEntity<Void> validate() {
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "로그아웃",
            description = "사용자를 로그아웃 처리하고 refresh token 쿠키를 제거합니다."
    )
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
