package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@Builder
public class ExtendedItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private LastNextBooking lastBooking;
    private LastNextBooking nextBooking;
    private List<CommentResponseDto> comments;

    public record LastNextBooking(Long id, Long bookerId) {
    }
}
