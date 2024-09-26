package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDtoUpdated {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
