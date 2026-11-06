package bienew.blog.backend.auth.controller;

import bienew.blog.backend.auth.dto.AuthResponse;
import bienew.blog.backend.auth.dto.AuthUserResponse;
import bienew.blog.backend.auth.service.AuthService;
import bienew.blog.backend.auth.service.AuthSession;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Profile("dev")
@ConditionalOnProperty(prefix = "app.auth", name = "dev-login-enabled", havingValue = "true")
public class DevAuthController {

    private static final String REFRESH_COOKIE = "refresh_token";
    private static final String COOKIE_PATH = "/api/v1/auth";

    private final AuthService authService;

    @Value("${app.auth.cookie-secure:false}")
    private boolean cookieSecure;

    @Operation(
            summary = "개발용 관리자 로그인",
            description = "로컬 Swagger 테스트용 관리자 액세스 토큰과 refresh 쿠키를 발급합니다. dev 프로필과 DEV_AUTH_ENABLED=true가 필요합니다."
    )
    @PostMapping("/dev")
    public ResponseEntity<AuthResponse> loginAsDevAdmin() {
        AuthSession session = authService.loginAsDevAdmin();
        AuthResponse body = new AuthResponse(
                session.accessToken(),
                "Bearer",
                authService.getAccessTokenLifetimeSeconds(),
                AuthUserResponse.from(session.user())
        );
        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_COOKIE, session.refreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path(COOKIE_PATH)
                .maxAge(Duration.ofSeconds(authService.getRefreshTokenLifetimeSeconds()))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);
    }
}
