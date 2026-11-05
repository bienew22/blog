package bienew.blog.backend.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
        @NotBlank(message = "카테고리 이름은 필수 입력 값입니다.")
        String categoryName,

        @NotBlank(message = "카테고리 설명은 필수 입력 값입니다.")
        String categoryDesc
) {
}
