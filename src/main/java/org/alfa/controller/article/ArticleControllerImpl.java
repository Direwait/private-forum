package org.alfa.controller.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alfa.service._dto.ArticleDto;
import org.alfa.service._dto.ArticlePreviewDto;
import org.alfa.service.article.ArticleService;
import org.alfa.service.auth.jwt.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleControllerImpl implements ArticleController {

    private final ArticleService articleService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    @Override
    public ResponseEntity<ArticleDto> createArticle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ArticleDto articleDto
    ) {
        return ResponseEntity.ok(articleService.createArticle(articleDto, userDetails.id()));
    }

    @GetMapping("/{articleId}")
    @Override
    public ResponseEntity<ArticleDto> getArticle(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID articleId) {
        ArticleDto dto = articleService.getArticleById(articleId, userDetails.id());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{articleId}")
    @Override
    public ResponseEntity<Void> deleteArticle(@PathVariable UUID articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    @Override
    public ResponseEntity<Page<ArticlePreviewDto>> getAllArticles(
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(articleService.getAllArticles(pageable));
    }
}
