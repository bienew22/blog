package bienew.blog.backend.post.controller;


import bienew.blog.backend.post.dto.PostSummaryResponse;
import bienew.blog.backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("")
    public List<PostSummaryResponse> getPosts() {
        return postService.getPosts();
    }
}
