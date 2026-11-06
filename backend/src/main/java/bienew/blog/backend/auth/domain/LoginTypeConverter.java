package bienew.blog.backend.auth.domain;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LoginTypeConverter implements Converter<String, LoginType> {
    @Override
    public LoginType convert(@NonNull String source) {
        return LoginType.fromString(source);
    }
}
