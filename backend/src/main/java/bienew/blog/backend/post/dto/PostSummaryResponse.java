package bienew.blog.backend.post.dto;

import bienew.blog.backend.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostSummaryResponse(
        String title,
        String author,
        LocalDateTime createAt,
        String slug,
        List<String> tags
) {
    public static PostSummaryResponse of(Post article) {
        return new PostSummaryResponse(
                article.getTitle(),
                article.getAuthor(),
                article.getCreateAt(),
                article.getSlug(),
                List.of("h1", "h2", "h3"));
    }
}
