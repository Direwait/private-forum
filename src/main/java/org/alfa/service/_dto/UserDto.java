package org.alfa.service._dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;

    @NotBlank
    @Size(min = 2, max = 16)
    private String name;

    @NotBlank
    @Size(min = 6, max = 16)
    private String password;

    private String role;

}
