package bienew.blog.backend.post.dto;

import bienew.blog.backend.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostSummaryResponse(
        String title,
        String author,
        LocalDateTime createAt,
        String slug,
        List<String> tags
) {
    public static PostSummaryResponse of(Post post) {
        return new PostSummaryResponse(
                post.getTitle(),
                post.getAuthor(),
                post.getCreateAt(),
                post.getSlug(),
                post.getPostTags().stream()
                        .map(postTag -> postTag.getTag().getTagName())
                        .toList()
        );
    }
}
