package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemsId = 0L;

    @Override
    public Item create(Item item) {
        item.setId(++itemsId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = items.get(item.getId());
        if (item.getName() != null && !item.getName().isBlank()) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        items.put(updatedItem.getId(), updatedItem);
        return updatedItem;
    }

    @Override
    public Item findItemByItemId(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> findItemsByUserId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> findItemByText(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .toList();
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public void delete(Long itemId) {
        items.remove(itemId);
    }
}
