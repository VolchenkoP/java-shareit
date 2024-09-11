package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER = "X-Sharer-User-Id";
    private final ItemService service;

    @GetMapping
    public List<ItemDTO> getItemsByUserId(@RequestHeader(HEADER) Long userId) {
        log.info("Получение всех объектов проката по id: {} пользователя", userId);
        return service.findItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItemDTOById(@PathVariable Long itemId) {
        log.info("Получение объекта проката по id: {}", itemId);
        return service.findItemByItemId(itemId);
    }

    @GetMapping("/search")
    public List<ItemDTO> searchItemByText(@RequestParam(defaultValue = "") String text) {
        log.info("Поиск объектов проката по тексту: {}", text);
        return service.findItemsByText(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO create(@RequestHeader(HEADER) Long userId,
                          @Valid @RequestBody ItemDTO itemDTO) {
        log.info("Слздание нового объекта проката пользователем с id: {}", userId);
        return service.create(userId, itemDTO);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO update(@RequestHeader(HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDTO itemDTO) {
        log.info("Обновление объекта проката с id: {} пользователем с id: {}", itemId, userId);
        return service.update(userId, itemId, itemDTO);
    }
}
