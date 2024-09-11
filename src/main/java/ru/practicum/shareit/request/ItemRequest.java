package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    @NotNull
    public Long id;
    @NotNull
    private String description;
    @NotNull
    private User requester;
    @NotNull
    private LocalDateTime created;
}
