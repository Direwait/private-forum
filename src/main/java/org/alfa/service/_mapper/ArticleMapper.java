package org.alfa.service._mapper;

import org.alfa.database.model.Article;
import org.alfa.service._dto.ArticleDto;

import org.alfa.service._dto.ArticlePreviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ArticleMapper {

    @Mapping(target = "authorId", source = "author.id")
    ArticleDto modelToDto(Article article);

    @Mapping(source = "authorId", target = "author.id")
    Article dtoToModel(ArticleDto dto);

    @Mapping(target = "authorId", source = "author.id")
    ArticlePreviewDto modelToPreviewDto(Article article);
}
