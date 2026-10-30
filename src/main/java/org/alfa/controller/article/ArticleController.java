package org.alfa.controller.article;

import org.alfa.service._dto.ArticleDto;
import org.alfa.service._dto.ArticlePreviewDto;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ArticleController {

    ResponseEntity<ArticleDto> createArticle(
            CustomUserDetails userDetails,
            ArticleDto articleDto
    );

    ResponseEntity<ArticleDto> getArticle(
            CustomUserDetails userDetails,
            UUID articleId
    );

    ResponseEntity<Void> deleteArticle(UUID articleId);

    ResponseEntity<Page<ArticlePreviewDto>> getAllArticles(@PageableDefault(size = 20) Pageable pageable);

}
