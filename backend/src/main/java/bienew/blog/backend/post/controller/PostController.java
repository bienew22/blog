package bienew.blog.backend.post.controller;


import bienew.blog.backend.post.dto.PostCreateRequest;
import bienew.blog.backend.post.dto.PostDetailResponse;
import bienew.blog.backend.post.dto.PostResponse;
import bienew.blog.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{slug}")
    @Operation(
            summary = "게시물 상세 조회",
            description = "지정된 슬러그에 해당하는 게시물의 상세 정보를 반환합니다."
    )
    public PostDetailResponse getPost(@PathVariable String slug) {
        return postService.getPost(slug);
    }

    @PostMapping("")
    @Operation(
            summary = "게시물 생성",
            description = "새로운 게시물을 생성합니다."
    )
    public PostDetailResponse createPost(@Valid @RequestBody PostCreateRequest postCreateRequest) {
        return postService.createPost(postCreateRequest);
    }


}
