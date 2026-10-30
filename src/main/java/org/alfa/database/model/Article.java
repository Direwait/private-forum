package org.alfa.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.alfa.database.enums.SubTier;

import java.util.UUID;

@Entity
@Table(name = "articles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "access_level")
    @Enumerated(EnumType.STRING)
    private SubTier accessLevel;

    @Column(name = "article_view")
    private long articleView;
}
