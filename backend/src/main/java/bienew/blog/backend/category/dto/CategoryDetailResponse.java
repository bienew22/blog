package bienew.blog.backend.category.dto;

import bienew.blog.backend.category.entity.Category;
import bienew.blog.backend.post.dto.PostSummaryResponse;

import java.util.List;

public record CategoryDetailResponse(
        String categoryName,
        String categoryDesc,
        int postCount,
        List<PostSummaryResponse> posts
) {
    public static CategoryDetailResponse from(Category category, List<PostSummaryResponse> posts) {

        return new CategoryDetailResponse(
                category.getCategoryName(),
                category.getCategoryDesc(),
                posts.size(),
                posts
        );
    }
}
