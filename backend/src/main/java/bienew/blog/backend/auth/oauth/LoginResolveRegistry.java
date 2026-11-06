package bienew.blog.backend.auth.oauth;

import bienew.blog.backend.auth.domain.LoginType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LoginResolveRegistry {

    private final Map<LoginType, LoginResolver> resolvers;

    public LoginResolveRegistry(List<LoginResolver> resolverList) {
        this.resolvers = resolverList.stream()
                .collect(Collectors.toMap(
                        LoginResolver::supports,
                        Function.identity(),
                        (existing, resolver) -> {
                            throw new IllegalStateException("Duplicate LoginResolver for login type: " + existing.supports());
                        },
                        () -> new java.util.EnumMap<>(LoginType.class)
                ));
    }

    public LoginResolver get(LoginType loginType) {
        LoginResolver resolver = resolvers.get(loginType);
        if (resolver == null) {
            throw new IllegalArgumentException("No LoginResolver found for login type: " + loginType);
        }
        return resolver;
    }
}
