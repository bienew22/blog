package bienew.blog.backend.post.service;


import bienew.blog.backend.auth.domain.UserRole;
import bienew.blog.backend.auth.entity.User;
import bienew.blog.backend.auth.repository.UserRepository;
import bienew.blog.backend.category.entity.Category;
import bienew.blog.backend.category.repository.CategoryRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    private final Snowflake snowflake;

    /**
     * 게시물 목록 조회
     * 1. 기본적으로 PUBLISHED 상태의 게시물만 조회하도록 설정
     * 2. 사용자가 임시상태 게시글 조회 가능 여부 확인
     * 3. 조회된 게시물 목록을 PostResponse로 변환하여 반환
     * @param userId 사용자 ID (JWT에서 추출)
     * @param statuses 조회할 게시물 상태 목록 (옵션)
     * @return 게시물 요약 정보 목록
     */
    public List<PostResponse> getPosts(String userId, List<PostStatus> statuses) {

        // 1. 조회할 상태 set
        Set<PostStatus> statusSet = new HashSet<>();

        if (statuses != null && !statuses.isEmpty()) {
            statusSet.addAll(statuses);
        } else {
            // 기본적으로 PUBLISHED 상태의 게시물만 조회하도록 설정
            statusSet.add(PostStatus.PUBLISHED);
        }

        // 2. 사용자가 임시상태 게시글 조회 가능 여부 확인
        if (statusSet.contains(PostStatus.DRAFT)) {
            if (userId == null) {
                throw new IllegalArgumentException("Unauthorized access to posts");
            }

            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
            if (user.getRole() != UserRole.ADMIN) {
                throw new IllegalArgumentException("Unauthorized access to posts");
            }
        }

        return postRepository.findAllWithTagsByStatus(statusSet).stream()
                .map(PostResponse::of)
                .toList();
    }
    
    public PostDetailResponse getPost(Long id) {
        Post post = postRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        return PostDetailResponse.of(post);
    }

    public PostDetailResponse getPostForEdit(Long id) {
        Post post = postRepository.findByIdWithDetails(id)
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

        // 2. 카테고리 조회
        Category category = categoryRepository.findById(postCreateRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + postCreateRequest.categoryId()));

        Post post = Post.create(
                snowflake.nextId(),
                postCreateRequest.title(),
                postCreateRequest.title(),
                postCreateRequest.contentMd(),
                MarkDownParser.toHtml(postCreateRequest.contentMd()),
                "bienew22",
                tags,
                PostStatus.fromString(postCreateRequest.status()),
                category
        );

        Post savedPost = postRepository.save(post);
        return PostDetailResponse.of(savedPost);
    }

    @Transactional
    public PostDetailResponse updatePost(Long id, PostCreateRequest postUpdateRequest) {
        Post post = postRepository.findByIdWithDetails(id)
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

        // 카테고리 변경
        Category category = categoryRepository.findById(postUpdateRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + postUpdateRequest.categoryId()));

        post.update(
                postUpdateRequest.title(),
                postUpdateRequest.contentMd(),
                MarkDownParser.toHtml(postUpdateRequest.contentMd()),
                PostStatus.fromString(postUpdateRequest.status()),
                category
        );

        return PostDetailResponse.of(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
