package bienew.blog.backend.tag.service;


import bienew.blog.backend.common.snowflake.Snowflake;
import bienew.blog.backend.post.domain.PostStatus;
import bienew.blog.backend.post.repository.PostRepository;
import bienew.blog.backend.tag.entity.Tag;
import bienew.blog.backend.post.dto.PostSummaryResponse;
import bienew.blog.backend.tag.dto.TagCreateRequest;
import bienew.blog.backend.tag.dto.TagDetailResponse;
import bienew.blog.backend.tag.dto.TagSummaryResponse;
import bienew.blog.backend.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final Snowflake snowflake;


    public List<TagSummaryResponse> getTags() {
        return tagRepository.findAllTagSummary(List.of(PostStatus.PUBLISHED));
    }

    public TagDetailResponse getTagPosts(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName).orElseThrow(() -> new RuntimeException("Tag not found"));
        return TagDetailResponse.of(
                tag,
                postRepository.findAllByTagNameAndStatusIn(tagName, List.of(PostStatus.PUBLISHED)).stream()
                        .map(PostSummaryResponse::of)
                        .toList()
        );
    }

    @Transactional
    public TagSummaryResponse createTag(TagCreateRequest request) {
        log.info("Creating new tag: {}", request.tagName());

        tagRepository.findByTagName(request.tagName()).ifPresent(existingTag -> {
            throw new RuntimeException("Tag already exists");
        });

        Tag tag = Tag.create(snowflake.nextId(), request.tagName(), request.tagDesc().trim());

        tagRepository.save(tag);

        return new TagSummaryResponse(tag.getTagName(), 0L);
    }
}
