package bienew.blog.backend.tag.service;


import bienew.blog.backend.common.snowflake.Snowflake;
import bienew.blog.backend.entity.PostTag;
import bienew.blog.backend.entity.Tag;
import bienew.blog.backend.post.dto.PostSummaryResponse;
import bienew.blog.backend.tag.dto.TagCreateRequest;
import bienew.blog.backend.tag.dto.TagPostResponse;
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
    private final Snowflake snowflake;


    public List<TagSummaryResponse> getTags() {
        return tagRepository.findAll().stream()
                .map(TagSummaryResponse::of)
                .toList();
    }

    public TagPostResponse getTagPosts(String tagName) {
        Tag tag = tagRepository.findByTagNameWithPosts(tagName).orElseThrow(() -> new RuntimeException("Tag not found"));
        return TagPostResponse.of(
                tag,
                tag.getPostTags().stream()
                        .map(PostTag::getPost)
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

        Tag tag = Tag.create(snowflake.nextId(), request.tagName(), request.tagName());

        tagRepository.save(tag);

        return TagSummaryResponse.of(tag);
    }
}
