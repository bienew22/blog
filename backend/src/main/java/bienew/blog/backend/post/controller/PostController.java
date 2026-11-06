package bienew.blog.backend.post.controller;


import bienew.blog.backend.category.dto.CategoryResponse;
import bienew.blog.backend.common.annotation.JwtUserId;
import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.post.dto.PostCreateRequest;
import bienew.blog.backend.post.dto.PostDetailResponse;
import bienew.blog.backend.post.dto.PostPreviewRequest;
import bienew.blog.backend.post.dto.PostResponse;
import bienew.blog.backend.markdown.MarkDownParser;
import bienew.blog.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "게시물 관리", description = "게시물 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "게시물 목록 조회",
            description = "모든 게시물의 요약 정보를 반환합니다. (로그인 되어 있는 경우 공개 외 상태의 게시글 조회 가능)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("")
    public List<PostResponse> getPosts(
            @RequestParam(required = false) List<PostStatus> statuses,
            @JwtUserId Long userId
    ) {
        return postService.getPosts(statuses, userId);
    }

    @Operation(
            summary = "게시물 상세 조회",
            description = "지정된 ID에 해당하는 게시물의 상세 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public PostDetailResponse getPost(
            @PathVariable Long id,
            @JwtUserId Long userId
    ) {
        return postService.getPost(id, userId);
    }

    @Operation(
            summary = "게시물 생성",
            description = "새로운 게시물을 생성합니다."
    )
    @PostMapping("")
    public PostDetailResponse createPost(
            @Valid @RequestBody PostCreateRequest postCreateRequest
    ) {
        return postService.createPost(postCreateRequest);
    }

    @Operation(
            summary = "게시물 수정",
            description = "게시물의 제목, 내용, 태그, 상태를 수정합니다."
    )
    @PutMapping("/{id}")
    public PostDetailResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostCreateRequest postUpdateRequest
    ) {
        return postService.updatePost(id, postUpdateRequest);
    }

    @Operation(
            summary = "게시물 삭제",
            description = "지정된 ID에 해당하는 게시물을 삭제합니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "게시물 미리보기",
            description = "저장하지 않고 마크다운을 HTML로 변환한 미리보기를 반환합니다."
    )
    @PostMapping("/preview")
    public PostDetailResponse previewPost(@RequestBody PostPreviewRequest request) {

        boolean isContentMdEmpty = request.contentMd() == null || request.contentMd().isBlank();

        return new PostDetailResponse(
                null,
                request.title() == null ? "" : request.title(),
                "preview",
                LocalDateTime.now(),
                MarkDownParser.toHtml(isContentMdEmpty ? "" : request.contentMd()),
                isContentMdEmpty ? "" : request.contentMd(),
                new CategoryResponse("1", "미리보기"),
                request.tags() == null ? List.of() : request.tags()
        );
    }
}
