package bienew.blog.backend.category.dto;

import bienew.blog.backend.category.entity.Category;

public record CategoryResponse(
        String categoryId,
        String categoryName
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId().toString(),
                category.getCategoryName()
        );
    }
}
