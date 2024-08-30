package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
