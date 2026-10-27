package bienew.blog.backend.auth.service;

import bienew.blog.backend.auth.domain.UserRole;
import bienew.blog.backend.auth.entity.User;
import bienew.blog.backend.auth.repository.UserRepository;
import bienew.blog.backend.common.snowflake.Snowflake;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Duration ACCESS_TOKEN_LIFETIME = Duration.ofMinutes(15);
    private final UserRepository userRepository;
    private final Snowflake snowflake;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder googleJwtDecoder;

    @Value("${app.auth.admin-emails:}")
    private String configuredAdminEmails;

    public AuthService(
            UserRepository userRepository,
            Snowflake snowflake,
            JwtEncoder jwtEncoder,
            @Qualifier("googleJwtDecoder") JwtDecoder googleJwtDecoder
    ) {
        this.userRepository = userRepository;
        this.snowflake = snowflake;
        this.jwtEncoder = jwtEncoder;
        this.googleJwtDecoder = googleJwtDecoder;
    }

    @Transactional
    public AuthSession loginWithGoogle(String credential) {
        Jwt googleToken;
        try {
            googleToken = googleJwtDecoder.decode(credential);
        } catch (JwtException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google ID token.", exception);
        }

        String email = googleToken.getClaimAsString("email");
        String googleSub = googleToken.getSubject();
        if (!Boolean.TRUE.equals(googleToken.getClaim("email_verified"))
                || email == null || googleSub == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google account email is not verified.");
        }

        UserRole role = isAdminEmail(email) ? UserRole.ADMIN : UserRole.USER;
        String name = valueOrDefault(googleToken.getClaimAsString("name"), email);
        User user = userRepository.findByGoogleSub(googleSub)
                // 기존 로그인 : 상태 업데이트
                .map(existing -> {
                    existing.updateProfile(
                            email,
                            name,
                            role
                    );
                    return existing;
                })
                // 신규 로그인 : 계정 생성
                .orElseGet(() -> createGoogleUser(email, googleSub, name, role));

        User savedUser = userRepository.save(user);

        return newSession(savedUser);
    }

    private User createGoogleUser(String email, String googleSub, String name, UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This email is already associated with another account."
            );
        }
        return userRepository.save(User.create(
                snowflake.nextId(),
                googleSub,
                email,
                name,
                role
        ));
    }

    public long getAccessTokenLifetimeSeconds() {
        return ACCESS_TOKEN_LIFETIME.toSeconds();
    }

    private AuthSession newSession(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("bienew-blog")
                .issuedAt(now)
                .expiresAt(now.plus(ACCESS_TOKEN_LIFETIME))
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", user.getRole().name())
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(),
                claims
        )).getTokenValue();

        return new AuthSession(accessToken, user);
    }

    private boolean isAdminEmail(String email) {
        Set<String> adminEmails = Arrays.stream(configuredAdminEmails.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return adminEmails.contains(email.toLowerCase());
    }

    private static String valueOrDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

}
