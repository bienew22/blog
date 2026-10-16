package bienew.blog.backend.post.service;


import bienew.blog.backend.common.snowflake.Snowflake;
import bienew.blog.backend.entity.Post;
import bienew.blog.backend.entity.Tag;
import bienew.blog.backend.markdown.MarkDownParser;
import bienew.blog.backend.post.dto.PostCreateRequest;
import bienew.blog.backend.post.dto.PostDetailResponse;
import bienew.blog.backend.post.dto.PostResponse;
import bienew.blog.backend.post.repository.PostRepository;
import bienew.blog.backend.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final Snowflake snowflake;

    public List<PostResponse> getPosts() {

        return postRepository.findAllWithTags().stream()
                .map(PostResponse::of)
                .toList();
    }

    public PostDetailResponse getPost(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with slug: " + slug));
        return PostDetailResponse.of(post);
    }

    @Transactional
    public PostDetailResponse createPost(PostCreateRequest postCreateRequest) {

        // 0. Slug 중복 체크
//        if (postRepository.existsBySlug(postCreateRequest.title())) {
//            throw new IllegalArgumentException("Slug already exists: " + postCreateRequest.title());
//        }

        // 1. tags 목록 조회
        List<Tag> tags = tagRepository.findAllByTagNameIn(postCreateRequest.tags());

        Post post = Post.create(
                snowflake.nextId(),
                postCreateRequest.title(),
                postCreateRequest.title(),
                postCreateRequest.contentMd(),
                MarkDownParser.toHtml(postCreateRequest.contentMd()),
                "bienew22",
                tags
        );

        // 2. 각 tag에 post_cnt 증가
        tagRepository.incrementPostCount(tags.stream().map(Tag::getTagName).toList());

        Post savedPost = postRepository.save(post);
        return PostDetailResponse.of(savedPost);
    }
}
