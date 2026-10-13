package bienew.blog.backend.tag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TagCreateRequest(
        @JsonProperty("tagName") String tagName
) {
}
