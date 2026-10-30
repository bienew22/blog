package bienew.blog.backend.post.controller;


import bienew.blog.backend.post.dto.PostCreateRequest;
import bienew.blog.backend.post.dto.PostDetailResponse;
import bienew.blog.backend.post.dto.PostPreviewRequest;
import bienew.blog.backend.post.dto.PostResponse;
import bienew.blog.backend.markdown.MarkDownParser;
import bienew.blog.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("")
    @Operation(
            summary = "게시물 목록 조회",
            description = "모든 게시물의 요약 정보를 반환합니다."
    )
    public List<PostResponse> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/drafts")
    @Operation(
            summary = "임시저장 게시물 목록 조회",
            description = "임시저장된 게시물의 요약 정보를 반환합니다."
    )
    public List<PostResponse> getDraftPosts() {
        return postService.getDraftPosts();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "게시물 상세 조회",
            description = "지정된 ID에 해당하는 게시물의 상세 정보를 반환합니다."
    )
    public PostDetailResponse getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/{id}/edit")
    @Operation(
            summary = "게시물 수정 정보 조회",
            description = "수정에 필요한 게시물 정보를 반환합니다."
    )
    public PostDetailResponse getPostForEdit(@PathVariable Long id) {
        return postService.getPostForEdit(id);
    }

    @PostMapping("")
    @Operation(
            summary = "게시물 생성",
            description = "새로운 게시물을 생성합니다."
    )
    public PostDetailResponse createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        return postService.createPost(postCreateRequest);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "게시물 수정",
            description = "게시물의 제목, 내용, 태그, 상태를 수정합니다."
    )
    public PostDetailResponse updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostCreateRequest postUpdateRequest
    ) {
        return postService.updatePost(id, postUpdateRequest);
    }

    @PostMapping("/preview")
    @Operation(
            summary = "게시물 미리보기",
            description = "저장하지 않고 마크다운을 HTML로 변환한 미리보기를 반환합니다."
    )
    public PostDetailResponse previewPost(@RequestBody PostPreviewRequest request) {
        return new PostDetailResponse(
                null,
                request.title() == null ? "" : request.title(),
                "preview",
                LocalDateTime.now(),
                MarkDownParser.toHtml(request.contentMd() == null ? "" : request.contentMd()),
                request.contentMd() == null ? "" : request.contentMd(),
                request.tags() == null ? List.of() : request.tags()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "게시물 삭제",
            description = "지정된 ID에 해당하는 게시물을 삭제합니다."
    )
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
