package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
