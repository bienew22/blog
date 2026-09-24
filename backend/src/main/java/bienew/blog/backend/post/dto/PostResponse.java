package bienew.blog.backend.post.dto;

import bienew.blog.backend.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        String title,
        String author,
        LocalDateTime createAt,
        String slug,
        List<String> tags
) {
    public static PostResponse of(Post post) {
        return new PostResponse(
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
