package org.alfa.service.article;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.alfa.database.model.Article;
import org.alfa.service._dto.ArticleDto;
import org.alfa.database.ArticleRepository;
import org.alfa.service._dto.ArticlePreviewDto;
import org.alfa.service._mapper.ArticleMapper;
import org.alfa.service.subscription.SubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final SubscriptionService subscriptionService;
    private final ArticleMapper articleMapper;


    @Transactional
    @Override
    public ArticleDto createArticle(ArticleDto articleDto, UUID userId) {
        articleDto.setAuthorId(userId);
        var article = articleMapper.dtoToModel(articleDto);
        return articleMapper.modelToDto(articleRepository.save(article));
    }

    @Transactional
    @Override
    public void deleteArticle(UUID articleId) {
        articleRepository.findById(articleId).ifPresent(articleRepository::delete);
    }

    @Transactional
    @Override
    public ArticleDto getArticleById(UUID articleId, UUID userId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found: " + articleId));

        if (!subscriptionService.hasAccess(userId, article.getAccessLevel())) {
            throw new AccessDeniedException("Subscription required: " + article.getAccessLevel());
        }

        incrementViews(articleId);
        return articleMapper.modelToDto(article);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ArticlePreviewDto> getAllArticles(Pageable pageable) {
        return articleRepository.findAll(pageable)
                .map(articleMapper::modelToPreviewDto);
    }

    @Override
    public void incrementViews(UUID articleId) {
        articleRepository.incrementViews(articleId);
    }
}
