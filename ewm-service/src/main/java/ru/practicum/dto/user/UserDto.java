package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    @Size(min = 2)
    @Size(max = 250)
    private String name;

    @NotBlank
    @Email
    @Size(min = 6)
    @Size(max = 254)
    private String email;
}
