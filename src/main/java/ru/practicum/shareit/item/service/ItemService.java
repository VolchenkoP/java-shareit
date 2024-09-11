package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.List;

public interface ItemService {

    ItemDTO create(Long userId, ItemDTO itemDTO);

    ItemDTO update(Long userId, Long itemId, ItemDTO itemDTO);

    ItemDTO findItemByItemId(Long itemId);

    List<ItemDTO> findItemsByUserId(Long userId);

    List<ItemDTO> findItemsByText(String text);

    void delete(Long itemId);
}
