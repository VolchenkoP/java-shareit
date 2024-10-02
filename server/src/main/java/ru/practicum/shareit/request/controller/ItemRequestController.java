package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_HEADER = HttpHeaders.USER_HEADER;

    private final ItemRequestService itemRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestResponseDto> findAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Получение всех запросов");
        return itemRequestService.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> findAllWithParamsFromAndSize(@RequestHeader(USER_HEADER) Long userId,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "20") int size) {
        log.info("Получение всех запросов с параметрами начального поиска: {} и в количестве: {}", from, size);
        return itemRequestService.findAllWithParamsFromAndSize(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestResponseDto findItemRequestById(@RequestHeader(USER_HEADER) Long userId,
                                                      @PathVariable Long requestId) {
        log.info("Получение запроса по id: {}", requestId);
        return itemRequestService.findItemRequestById(userId, requestId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                         @RequestBody ItemRequestDtoToAdd itemRequestDto) {
        log.info("Добавление нового запроса");
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }
}
