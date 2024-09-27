package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class ItemCreateDto {
    private  Long id;
    private String name;
    private String description;
    private User owner;
    private Boolean available;
    private Long requestId;
}
