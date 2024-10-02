package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItemsByUser(@RequestHeader(HttpHeaders.USER_HEADER) Long userId) {
        log.info("Получение всех объектов проката по id: {} пользователя", userId);
        return itemClient.findItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItemById(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                               @PathVariable Long itemId) {
        log.info("Получение объекта проката по id: {}", itemId);
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findItemsByText(@RequestParam(defaultValue = "") String text) {
        log.info("Поиск объектов проката по тексту: {}", text);
        return itemClient.findItemsByText(text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                         @Valid @RequestBody ItemDto itemDTO) {
        log.info("Создание нового объекта проката пользователем с id: {}", userId);
        return itemClient.create(userId, itemDTO);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentRequestDto requestDto) {
        log.info("Добавление комментария к объекту проката с id: {} пользователем с id: {}", itemId, userId);
        return itemClient.addComment(userId, itemId, requestDto);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                         @PathVariable Long itemId, // mb int
                                         @RequestBody ItemFromUpdateRequestDto itemDTO) {
        log.info("Обновление объекта проката с id: {} пользователем с id: {}", itemId, userId);
        return itemClient.update(userId, itemId, itemDTO);
    }
}
