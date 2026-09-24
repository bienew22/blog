package bienew.blog.backend.tag.dto;

import bienew.blog.backend.entity.Tag;

public record TagSummaryResponse(
        String tagName,
        Integer postCount
) {
    public static TagSummaryResponse of(Tag tag) {
        return new TagSummaryResponse(tag.getTagName(), tag.getPostCnt());
    }
}
