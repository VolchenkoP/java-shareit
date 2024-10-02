package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.config.constants.HttpHeaders;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequest(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                                 @Valid @RequestBody ItemRequestDtoToAdd itemRequestAddDto) {
        log.info("Добавление нового запроса: {}, пользователем с id: {}", itemRequestAddDto, userId);
        return requestClient.addItemRequest(userId, itemRequestAddDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequests(@RequestHeader(HttpHeaders.USER_HEADER) Long userId) {
        log.info("Поиск всех запросов пользователя с id: {}", userId);
        return requestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                                     @RequestParam(defaultValue = "0")
                                                     @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "20")
                                                     @Positive int size) {
        log.info("Поиск всех запросов с параметрами начала: {} и количества: {}", from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequest(@RequestHeader(HttpHeaders.USER_HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Получение запроса с id: {}", requestId);
        return requestClient.getItemRequest(userId, requestId);
    }
}
