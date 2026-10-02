package bienew.blog.backend.post.dto;

import bienew.blog.backend.entity.Post;

import java.time.LocalDateTime;

public record PostSummaryResponse(
        String title,
        String slug,
        LocalDateTime createAt
) {
    public static PostSummaryResponse of(Post post) {
        return new PostSummaryResponse(post.getTitle(), post.getSlug(), post.getCreateAt());
    }
}
