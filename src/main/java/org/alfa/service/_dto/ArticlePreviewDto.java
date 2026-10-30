package org.alfa.service._dto;

import org.alfa.database.enums.SubTier;

import java.time.LocalDateTime;
import java.util.UUID;

public record ArticlePreviewDto(
        UUID id,
        String title,
        UUID authorId,
        SubTier accessLevel,
        int views,
        LocalDateTime createdAt
) {}