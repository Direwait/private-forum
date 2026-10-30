package org.alfa.service.article;

import org.alfa.service._dto.ArticleDto;
import org.alfa.service._dto.ArticlePreviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ArticleService {

    ArticleDto createArticle(ArticleDto articleDto, UUID userId);

    void deleteArticle(UUID articleId);

    ArticleDto getArticleById(UUID articleId, UUID userId);

    Page<ArticlePreviewDto> getAllArticles(Pageable pageable);

    void incrementViews(UUID articleId);
}
