package bienew.blog.backend.auth.oauth;


import bienew.blog.backend.auth.domain.LoginType;
import bienew.blog.backend.auth.dto.OauthUserInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GoogleLoginResolver implements LoginResolver {

    private final JwtDecoder googleJwtDecoder;

    public GoogleLoginResolver(@Qualifier("googleJwtDecoder") JwtDecoder googleJwtDecoder) {
        this.googleJwtDecoder = googleJwtDecoder;
    }

    @Override
    public LoginType supports() {
        return LoginType.GOOGLE;
    }

    @Override
    public OauthUserInfo resolve(String credential) {
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

        // email, name, subject를 OauthUserInfo로 반환
        return OauthUserInfo.of(
                email,
                valueOrDefault(googleToken.getClaimAsString("name"), email),
                googleSub
        );
    }

    private String valueOrDefault(String value, String defaultValue) {
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}
