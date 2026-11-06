package bienew.blog.backend.post.domain;

import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostStatusConverter implements Converter<String, PostStatus> {
    @Override
    public PostStatus convert(@NonNull String source) {
        return PostStatus.fromString(source);
    }
}
