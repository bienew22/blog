package bienew.blog.backend.post.dto;

import bienew.blog.backend.post.entity.Post;

import java.time.LocalDateTime;

public record PostSummaryResponse(
        String id,
        String title,
        String slug,
        LocalDateTime createAt
) {
    public static PostSummaryResponse of(Post post) {
        return new PostSummaryResponse(post.getId().toString(), post.getTitle(), post.getSlug(), post.getCreateAt());
    }
}
