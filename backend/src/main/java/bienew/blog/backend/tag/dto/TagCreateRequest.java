package bienew.blog.backend.tag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record TagCreateRequest(
        @JsonProperty("tagName")
        @NotBlank(message = "태그 이름은 필수입니다.")
        String tagName,

        @JsonProperty("tagDesc")
        @NotBlank(message = "태그 설명은 필수입니다.")
        String tagDesc
) {
}
