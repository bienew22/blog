package bienew.blog.backend.tag.service;


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
}
