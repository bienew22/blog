package bienew.blog.backend.tag.controller;


import bienew.blog.backend.tag.dto.TagSummaryResponse;
import bienew.blog.backend.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping("")
    public List<TagSummaryResponse> getTags() {
        return tagService.getTags();
    }
}
