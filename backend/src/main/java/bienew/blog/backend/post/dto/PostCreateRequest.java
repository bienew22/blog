package bienew.blog.backend.post.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PostCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String contentMd,

        @NotBlank(message = "상태는 필수입니다.")
        String status,

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        List<String> tags
) {
}
