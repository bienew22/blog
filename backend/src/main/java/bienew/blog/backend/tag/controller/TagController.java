package bienew.blog.backend.tag.controller;


import bienew.blog.backend.tag.dto.TagCreateRequest;
import bienew.blog.backend.tag.dto.TagPostResponse;
import bienew.blog.backend.tag.dto.TagSummaryResponse;
import bienew.blog.backend.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@Tag(name = "태그 관리", description = "태그 관련 API")
public class TagController {

    private final TagService tagService;

    @GetMapping("")
    @Operation(
            summary = "태그 목록 조회",
            description = "모든 태그와 해당 태그에 속한 게시물 수를 반환합니다."
    )
    public List<TagSummaryResponse> getTags() {
        return tagService.getTags();
    }

    @GetMapping("/{tagName}")
    @Operation(
            summary = "태그로 게시물 조회",
            description = "지정된 태그와 연관된 게시물 목록을 반환합니다."
    )
    public TagPostResponse getTag(@PathVariable String tagName) {
        return tagService.getTagPosts(tagName);
    }

    @PostMapping("")
    @Operation(
            summary = "태그 생성",
            description = "지정된 이름으로 새로운 태그를 생성합니다. 태그가 이미 존재하는 경우 오류가 반환됩니다."
    )
    public TagSummaryResponse createTag(@RequestBody TagCreateRequest request) {
        return tagService.createTag(request);
    }
}
