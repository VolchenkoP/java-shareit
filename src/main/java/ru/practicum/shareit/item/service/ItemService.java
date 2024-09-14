package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;

import java.util.List;

public interface ItemService {

    ItemDTO create(Long userId, ItemDTO itemDTO);

    ItemDTO update(Long userId, Long itemId, ItemFromUpdateRequestDto itemDTO);

    ExtendedItemDto findItemByItemId(Long itemId, Long userId);

    List<ExtendedItemDto> findItemsByUserId(Long userId);

    List<ItemDTO> findItemsByText(String text);

    CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto);

    void delete(Long itemId);
}
