package bienew.blog.backend.article.dto;

import bienew.blog.backend.article.entity.Article;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleSummaryResponse(
        String title,
        String author,
        LocalDateTime createAt,
        String slug,
        List<String> tags
) {
    public static ArticleSummaryResponse of(Article article) {
        return new ArticleSummaryResponse(
                article.getTitle(),
                article.getAuthor(),
                article.getCreateAt(),
                article.getSlug(),
                List.of("h1", "h2", "h3"));
    }
}
