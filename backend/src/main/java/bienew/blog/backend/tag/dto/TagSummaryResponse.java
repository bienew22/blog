package bienew.blog.backend.tag.dto;

public record TagSummaryResponse(
        String tagName,
        Long postCount
) {
}
