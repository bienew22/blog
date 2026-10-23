package bienew.blog.backend.post.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;

public record PostPreviewRequest(
        String title,

        String contentMd,

        @JsonSetter(nulls = Nulls.AS_EMPTY)
        List<String> tags
) {
}
