package org.alfa.service._dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.alfa.database.enums.SubTier;
import org.alfa.database.enums.UserRole;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID id;

    private String title;

    private String content;

    private UUID authorId;

    private SubTier accessLevel;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long articleView;
}
