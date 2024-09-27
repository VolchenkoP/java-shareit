package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<ItemRequestResponseDto> findAll(@RequestHeader(USER_HEADER) Long userId) {
        return itemRequestService.findRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllWithParamsFromAndSize(@RequestHeader(USER_HEADER) Long userId,
                                                             @RequestParam(defaultValue = "0") int from,
                                                             @RequestParam(defaultValue = "20") int size) {
        return itemRequestService.findAllWithParamsFromAndSize(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto findItemRequestById(@RequestHeader(USER_HEADER) Long userId,
                                                      @PathVariable Long requestId) {
        return itemRequestService.findItemRequestById(userId, requestId);
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                         @RequestBody ItemRequestDtoToAdd itemRequestDto) {
        return itemRequestService.addItemRequest(userId, itemRequestDto);
    }
}
