package org.alfa.database;

import org.alfa.database.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @Modifying
    @Query("UPDATE Article a SET a.articleView = a.articleView + 1 WHERE a.id = :id")
    void incrementViews(@Param("id") UUID id);
}
