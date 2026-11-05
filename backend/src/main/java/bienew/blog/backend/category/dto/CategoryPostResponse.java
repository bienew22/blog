package bienew.blog.backend.category.dto;

import bienew.blog.backend.category.entity.Category;
import bienew.blog.backend.post.dto.PostSummaryResponse;

import java.util.List;

public record CategoryPostResponse(
        String categoryName,
        String categoryDesc,
        int postCount,
        List<PostSummaryResponse> posts
) {
    public static CategoryPostResponse from(Category category, List<PostSummaryResponse> posts) {

        return new CategoryPostResponse(
                category.getCategoryName(),
                category.getCategoryDesc(),
                posts.size(),
                posts
        );
    }
}
