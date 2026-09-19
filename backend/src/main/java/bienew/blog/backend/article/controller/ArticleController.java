package bienew.blog.backend.article.controller;


import bienew.blog.backend.article.dto.ArticleSummaryResponse;
import bienew.blog.backend.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public List<ArticleSummaryResponse> getArticles() {
        return articleService.getArticles();
    }
}
