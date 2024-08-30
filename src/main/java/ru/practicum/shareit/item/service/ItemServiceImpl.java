package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ItemDTO create(Long userId, ItemDTO itemDTO) {
        log.info("Создание нового объекта проката пользователем с Id: {}", userId);
        userService.userExistById(userId);
        Item item = itemMapper.fromDTO(itemDTO);
        item.setOwner(userMapper.fromDTO(userService.findUserById(userId)));
        return itemMapper.toDTO(repository.create(item));
    }

    @Override
    public ItemDTO update(Long userId, Long itemId, ItemDTO itemDTO) {
        log.info("Обновление данных по объекту проката с Id: {}", itemId);
        userService.userExistById(userId);
        isItemExistByItemId(itemId);
        isOwner(userId, itemId);
        Item updatedItem = itemMapper.fromDTO(itemDTO);
        updatedItem.setId(itemId);
        return itemMapper.toDTO(repository.update(updatedItem));
    }

    @Override
    public ItemDTO findItemByItemId(Long itemId) {
        log.info("Поиск объекта проката с Id: {}", itemId);
        isItemExistByItemId(itemId);
        return itemMapper.toDTO(repository.findItemByItemId(itemId));
    }

    @Override
    public List<ItemDTO> findItemsByUserId(Long userId) {
        log.info("Поиск объектов проката по пользователю с Id: {}", userId);
        userService.userExistById(userId);
        return repository.findItemsByUserId(userId).stream()
                .map(itemMapper::toDTO)
                .toList();
    }

    @Override
    public List<ItemDTO> findItemsByText(String text) {
        log.info("Поиск объекта проката по совпадению с поисковой строкой {}", text);
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }
        return repository.findItemByText(text).stream()
                .map(itemMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long itemId) {
        log.info("Удаление объекта проката с Id: {}", itemId);
        isItemExistByItemId(itemId);
        repository.delete(itemId);
        log.info("Объект проката с Id: {} удален", itemId);
    }

    private void isItemExistByItemId(Long itemId) {
        log.info("Проверка существования объекта проката с Id: {}", itemId);
        if (repository.findAll().stream()
                .noneMatch(item -> item.getId().equals(itemId))) {
            log.error("Объект проката с Id: {} не существует", itemId);
            throw new NotFoundException("Объект проката с Id: " + itemId + " не найден");
        }
    }

    private void isOwner(Long userId, Long itemId) {
        log.info("Проверка принадлежности объекта проката с Id: {} пользователю с Id: {}", itemId, userId);
        if (!repository.findItemByItemId(itemId).getOwner().getId().equals(userId)) {
            log.error("Пользователь с Id: {} не является создателем Объекта проката с Id: {}", userId, itemId);
            throw new ValidationException("Пользователь с Id: " + userId
                    + " не является создателем объекта проката с Id: " + itemId);
        }
    }
}
