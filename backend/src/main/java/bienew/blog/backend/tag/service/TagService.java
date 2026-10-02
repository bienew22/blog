package bienew.blog.backend.tag.service;


import bienew.blog.backend.entity.PostTag;
import bienew.blog.backend.entity.Tag;
import bienew.blog.backend.post.dto.PostSummaryResponse;
import bienew.blog.backend.tag.dto.TagPostResponse;
import bienew.blog.backend.tag.dto.TagSummaryResponse;
import bienew.blog.backend.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<TagSummaryResponse> getTags() {
        return tagRepository.findAll().stream()
                .map(TagSummaryResponse::of)
                .toList();
    }

    public TagPostResponse getTagPosts(String tagName) {
        Tag tag = tagRepository.findByName(tagName).orElseThrow(() -> new RuntimeException("Tag not found"));
        return TagPostResponse.of(
                tag,
                tag.getPostTags().stream()
                        .map(PostTag::getPost)
                        .map(PostSummaryResponse::of)
                        .toList()
        );
    }
}
