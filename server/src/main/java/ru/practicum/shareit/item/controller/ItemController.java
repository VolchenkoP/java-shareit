package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.config.constants.HttpHeaders;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExtendedItemDto> findItemsByUser(@RequestHeader(HttpHeaders.USER_HEADER) Long userId) {
        log.info("Получение всех объектов проката по id: {} пользователя", userId);
        return service.findItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ExtendedItemDto findItemById(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                        @PathVariable Long itemId) {
        log.info("Получение объекта проката по id: {}", itemId);
        return service.findItemById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDTO> findItemsByText(@RequestParam(defaultValue = "") String text) {
        log.info("Поиск объектов проката по тексту: {}", text);
        return service.findItemsByText(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO create(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                          @RequestBody ItemCreateDto itemDTO) {
        log.info("Создание нового объекта проката пользователем с id: {}", userId);
        return service.create(userId, itemDTO);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody CommentRequestDto requestDto) {
        log.info("Добавление комментария к объекту проката с id: {} пользователем с id: {}", itemId, userId);
        return service.addComment(userId, itemId, requestDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO update(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemFromUpdateRequestDto itemDTO) {
        log.info("Обновление объекта проката с id: {} пользователем с id: {}", itemId, userId);
        return service.update(userId, itemId, itemDTO);
    }
}
