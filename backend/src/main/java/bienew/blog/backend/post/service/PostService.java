package bienew.blog.backend.post.service;


import bienew.blog.backend.post.dto.PostSummaryResponse;
import bienew.blog.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<PostSummaryResponse> getPosts() {

        return postRepository.findAllWithTags().stream()
                .map(PostSummaryResponse::of)
                .toList();
    }
}
