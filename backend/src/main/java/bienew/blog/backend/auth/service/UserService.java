package bienew.blog.backend.auth.service;

import bienew.blog.backend.auth.domain.UserRole;
import bienew.blog.backend.auth.dto.OauthUserInfo;
import bienew.blog.backend.auth.entity.User;
import bienew.blog.backend.auth.repository.UserRepository;
import bienew.blog.backend.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final Snowflake snowflake;

    @Value("${app.auth.admin-emails:}")
    private String configuredAdminEmails;

    @Transactional
    public User findOrRegisterUser(OauthUserInfo userInfo) {
        UserRole role = isAdminEmail(userInfo.email()) ? UserRole.ADMIN : UserRole.USER;

        User user = userRepository.findByGoogleSub(userInfo.sub())
                // 기존 로그인 : 상태 업데이트
                .map(existing -> {
                    existing.updateProfile(
                            userInfo.email(),
                            userInfo.name(),
                            role
                    );
                    return existing;
                })
                // 신규 로그인 : 계정 생성
                .orElseGet(() -> createUser(userInfo.email(), userInfo.sub(), userInfo.name(), role));

        return userRepository.save(user);
    }

    private User createUser(String email, String googleSub, String name, UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This email is already associated with another account."
            );
        }
        return User.create(
                snowflake.nextId(),
                googleSub,
                email,
                name,
                role
        );
    }

    private boolean isAdminEmail(String email) {
        Set<String> adminEmails = Arrays.stream(configuredAdminEmails.split(","))
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return adminEmails.contains(email.toLowerCase());
    }
}
