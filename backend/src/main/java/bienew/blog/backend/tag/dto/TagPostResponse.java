package bienew.blog.backend.tag.dto;

import bienew.blog.backend.tag.entity.Tag;
import bienew.blog.backend.post.dto.PostSummaryResponse;

import java.util.List;

public record TagPostResponse(
        String tagName,
        String tagDesc,
        int postCount,
        List<PostSummaryResponse> posts
) {
    public static TagPostResponse of(Tag tag, List<PostSummaryResponse> posts) {
        return new TagPostResponse(
                tag.getTagName(),
                tag.getTagDesc(),
                posts.size(),
                posts);
    }
}
