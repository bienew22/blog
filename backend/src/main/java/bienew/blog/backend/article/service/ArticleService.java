package bienew.blog.backend.article.service;


import bienew.blog.backend.article.dto.ArticleSummaryResponse;
import bienew.blog.backend.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleSummaryResponse> getArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleSummaryResponse::of)
                .toList();
    }
}
