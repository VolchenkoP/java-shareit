package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Long userId, ItemRequestDtoToAdd itemRequestDtoToAdd);

    List<ItemRequestResponseDto> findRequestByUserId(Long userId);

    List<ItemRequestDto> findAllWithParamsFromAndSize(Long userId, int from, int size);

    ItemRequestResponseDto findItemRequestById(Long userId, Long requestId);
}
