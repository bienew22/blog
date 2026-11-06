package bienew.blog.backend.category.controller;


import bienew.blog.backend.category.dto.CategoryCreateRequest;
import bienew.blog.backend.category.dto.CategoryDetailResponse;
import bienew.blog.backend.category.dto.CategoryResponse;
import bienew.blog.backend.category.dto.CategorySummaryResponse;
import bienew.blog.backend.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리 관리", description = "카테고리 관련 API")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            summary = "카테고리 생성",
            description = "새로운 카테고리를 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @Operation(
            summary = "카테고리 목록 조회",
            description = "모든 카테고리 이름과 ID를 반환합니다."
    )
    @GetMapping("")
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @Operation(
            summary = "카테고리 목록 조회",
            description = "모든 카테고리와 해당 카테고리에 속한 게시물 수를 반환합니다."
    )
    @GetMapping("/meta/summary")
    public List<CategorySummaryResponse> getCategoriesSummary() {
        return categoryService.getCategoriesSummary();
    }

    @Operation(
            summary = "카테고리로 상세 조회",
            description = "지정된 카테고리와 연관된 게시물 목록을 반환합니다."
    )
    @GetMapping("/{categoryName}")
    public CategoryDetailResponse getCategory(@PathVariable String categoryName) {
        return categoryService.getCategoryPosts(categoryName);
    }
}
