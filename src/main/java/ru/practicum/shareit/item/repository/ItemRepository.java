package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item create(Item item);

    Item update(Item item);

    Item findItemByItemId(Long itemId);

    List<Item> findItemsByUserId(Long userId);

    List<Item> findItemByText(String text);

    List<Item> findAll();

    void delete(Long itemId);
}
