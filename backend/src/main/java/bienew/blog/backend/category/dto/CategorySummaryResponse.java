package bienew.blog.backend.category.dto;

import bienew.blog.backend.category.entity.Category;

import java.time.LocalDateTime;

public record CategorySummaryResponse(
        String categoryName,
        String categoryDesc,
        Long postCount,
        LocalDateTime lastPostCreateAt
) {
    public static CategorySummaryResponse from(Category category, Long postCount, LocalDateTime lastPostCreateAt) {
        return new CategorySummaryResponse(
                category.getCategoryName(),
                category.getCategoryDesc(),
                postCount,
                lastPostCreateAt
        );
    }
}
