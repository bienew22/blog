package bienew.blog.backend.post.service;


import bienew.blog.backend.common.snowflake.Snowflake;
import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.post.entity.Post;
import bienew.blog.backend.tag.entity.Tag;
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

        return postRepository.findAllWithTagsByStatus(PostStatus.PUBLISHED).stream()
                .map(PostResponse::of)
                .toList();
    }

    public List<PostResponse> getDraftPosts() {
        return postRepository.findAllWithTagsByStatus(PostStatus.DRAFT).stream()
                .map(PostResponse::of)
                .toList();
    }

    public PostDetailResponse getPost(Long id) {
        Post post = postRepository.findByIdWithTags(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return PostDetailResponse.of(post);
    }

    public PostDetailResponse getPostForEdit(Long id) {
        Post post = postRepository.findByIdForEdit(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
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
                tags,
                PostStatus.fromString(postCreateRequest.status())
        );

        Post savedPost = postRepository.save(post);
        return PostDetailResponse.of(savedPost);
    }

    @Transactional
    public PostDetailResponse updatePost(Long id, PostCreateRequest postUpdateRequest) {
        Post post = postRepository.findByIdForEdit(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        List<Tag> tags = tagRepository.findAllByTagNameIn(postUpdateRequest.tags());

        List<String> currentTagNames = post.getPostTags().stream()
                .map(postTag -> postTag.getTag().getTagName())
                .toList();
        List<String> updatedTagNames = tags.stream()
                .map(Tag::getTagName)
                .toList();

        List<String> removedTagNames = currentTagNames.stream()
                .filter(tagName -> !updatedTagNames.contains(tagName))
                .toList();
        List<String> addedTagNames = updatedTagNames.stream()
                .filter(tagName -> !currentTagNames.contains(tagName))
                .toList();

        // 전체 삭제 후 재삽입하면 insert가 delete보다 먼저 실행되어 uk_post_tags 위반이 나므로 변경분만 반영
        post.removeTags(removedTagNames);
        post.addTags(tags.stream()
                .filter(tag -> addedTagNames.contains(tag.getTagName()))
                .toList());

        post.update(
                postUpdateRequest.title(),
                postUpdateRequest.contentMd(),
                MarkDownParser.toHtml(postUpdateRequest.contentMd()),
                PostStatus.fromString(postUpdateRequest.status())
        );

        return PostDetailResponse.of(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
