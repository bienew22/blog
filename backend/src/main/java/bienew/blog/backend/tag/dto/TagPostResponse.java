package bienew.blog.backend.tag.dto;

import bienew.blog.backend.entity.Tag;
import bienew.blog.backend.post.dto.PostSummaryResponse;

import java.util.List;

public record TagPostResponse(
        String tagName,
        String tagDesc,
        Integer postCount,
        List<PostSummaryResponse> posts
) {
    public static TagPostResponse of(Tag tag, List<PostSummaryResponse> posts) {
        return new TagPostResponse(
                tag.getTagName(),
                tag.getTagDesc(),
                tag.getPostTags().size(),
                posts);
    }
}
