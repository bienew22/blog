package bienew.blog.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Qualifier("appJwtDecoder") JwtDecoder appJwtDecoder,
            JwtAuthenticationConverter jwtAuthenticationConverter
    ) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Auth Controller API 관련 권한 정리
                        .requestMatchers(HttpMethod.GET, "/api/v1/auth/me")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**")
                        .permitAll()
                        // Post Controller API 관련 권한 정리
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**")
                        .permitAll()
                        .requestMatchers("/api/v1/posts/**")
                        .hasRole("ADMIN")
                        // Tag Controller API 관련 권한 정리
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**")
                        .permitAll()
                        .requestMatchers("/api/v1/tags/**")
                        .hasRole("ADMIN")
                        // Category Controller API 관련 권한 정리
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**")
                        .permitAll()
                        .requestMatchers("/api/v1/categories/**")
                        .hasRole("ADMIN")
                        // Swagger 관련 권한 정리
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt
                                .decoder(appJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }
}