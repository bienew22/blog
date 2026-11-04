package bienew.blog.backend.category.service;

import bienew.blog.backend.category.dto.CategoryCreateRequest;
import bienew.blog.backend.category.dto.CategoryPostResponse;
import bienew.blog.backend.category.dto.CategoryResponse;
import bienew.blog.backend.category.dto.CategorySummaryResponse;
import bienew.blog.backend.category.entity.Category;
import bienew.blog.backend.category.repository.CategoryRepository;
import bienew.blog.backend.common.snowflake.Snowflake;
import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.post.dto.PostSummaryResponse;
import bienew.blog.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final Snowflake snowflake;

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional
    public CategorySummaryResponse createCategory(CategoryCreateRequest request) {
        log.info("Creating new category: {}", request.categoryName());

        categoryRepository.findByCategoryName(request.categoryName()).ifPresent(existingCategory -> {
            throw new RuntimeException("Category already exists");
        });

        Category category = Category.create(
                snowflake.nextId(),
                request.categoryName(),
                request.categoryDesc().trim()
        );

        categoryRepository.save(category);

        return CategorySummaryResponse.from(category, 0L, null);
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public List<CategorySummaryResponse> getCategoriesSummary() {
        return categoryRepository.findAllCategorySummary(List.of(PostStatus.PUBLISHED));
    }

    public CategoryPostResponse getCategoryPosts(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return CategoryPostResponse.from(category,
                postRepository.findAllByCategoryNameAndStatusIn(categoryName, List.of(PostStatus.PUBLISHED)).stream()
                        .map(PostSummaryResponse::of)
                        .toList()
                );
    }
}
