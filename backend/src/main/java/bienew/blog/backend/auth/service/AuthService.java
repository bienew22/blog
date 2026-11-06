package bienew.blog.backend.auth.service;

import bienew.blog.backend.auth.dto.LoginRequest;
import bienew.blog.backend.auth.dto.OauthUserInfo;
import bienew.blog.backend.auth.entity.RefreshToken;
import bienew.blog.backend.auth.entity.User;
import bienew.blog.backend.auth.oauth.LoginResolveRegistry;
import bienew.blog.backend.auth.oauth.LoginResolver;
import bienew.blog.backend.auth.repository.RefreshTokenRepository;
import bienew.blog.backend.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final AuthProperties authProperties;

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Snowflake snowflake;
    private final JwtEncoder jwtEncoder;
    private final LoginResolveRegistry loginResolveRegistry;


    /**
     * 로그인 처리
     * 1. 로그인 타입과 credential 검증
     * 2. LoginResolver를 통해 OauthUserInfo를 가져옴
     * 3. UserService를 통해 사용자 조회 또는 등록
     * 4. 새로운 세션(AuthSession) 생성 및 반환
     * @param request 로그인 요청 정보
     * @return 새로운 세션 정보 (access token, refresh token, 사용자 정보)
     */
    public AuthSession login(LoginRequest request) {

        if (request.loginType() == null || request.credential() == null || request.credential().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login type and credential must be provided.");
        }

        LoginResolver resolver = loginResolveRegistry.get(request.loginType());

        OauthUserInfo userInfo = resolver.resolve(request.credential());

        User user = userService.findOrRegisterUser(userInfo);

        return newSession(user);
    }

    /**
     * 개발 환경에서 관리자 계정으로 로그인 처리
     * @return 새로운 세션 정보 (access token, refresh token, 사용자 정보)
     */
    public AuthSession loginAsDevAdmin() {
        String googleSub = "development-admin";
        String email = "developer@localhost";

        OauthUserInfo userInfo = OauthUserInfo.of(email, "Local Developer", googleSub);

        User user = userService.findOrRegisterUser(userInfo);

        return newSession(user);
    }

    /**
     * Refresh token을 사용하여 새로운 세션(AuthSession)을 생성
     * 1. refresh token 검증 (null, blank, 존재 여부)
     * 2. refresh token 만료 여부 확인
     * 3. 기존 refresh token 삭제 (1회용)
     * 4. 새로운 세션(AuthSession) 생성 및 반환
     * @param refreshToken 사용자가 제공한 refresh token
     * @return 새로운 세션 정보 (access token, refresh token, 사용자 정보)
     * @throws ResponseStatusException 401 Unauthorized: refresh token이 없거나 무효/만료된 경우
     */
    @Transactional
    public AuthSession refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is missing.");
        }
        String hash = hash(refreshToken);
        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token."));
        if (refreshTokenRepository.deleteByTokenHash(hash) == 0 || stored.isExpired()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired.");
        }
        return newSession(stored.getUser());
    }

    /**
     * 로그아웃 처리
     * 1. refresh token 검증 (null, blank)
     * 2. refresh token 삭제 (로그아웃 처리)
     * @param refreshToken 사용자가 제공한 refresh token
     */
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenRepository.deleteByTokenHash(hash(refreshToken));
        }
    }

    public long getAccessTokenLifetimeSeconds() {
        return authProperties.accessTokenLifetime().toSeconds();
    }

    public long getRefreshTokenLifetimeSeconds() {
        return authProperties.refreshTokenLifetime().toSeconds();
    }

    private String issueRefreshToken(User user) {
        byte[] random = new byte[48];
        SECURE_RANDOM.nextBytes(random);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(random);
        refreshTokenRepository.save(RefreshToken.create(
                snowflake.nextId(),
                user,
                hash(token),
                LocalDateTime.now().plus(authProperties.refreshTokenLifetime())
        ));
        return token;
    }

    private static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private AuthSession newSession(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("bienew-blog")
                .issuedAt(now)
                .expiresAt(now.plus(authProperties.accessTokenLifetime()))
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        )).getTokenValue();

        return AuthSession.of(accessToken, issueRefreshToken(user), user);
    }
}
