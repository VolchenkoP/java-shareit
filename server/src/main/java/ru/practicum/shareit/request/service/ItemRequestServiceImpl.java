package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestDto addItemRequest(Long userId, ItemRequestDtoToAdd itemRequestDtoToAdd) {
        User user = getUserById(userId);
        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDtoToAdd);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        ItemRequest createdItemRequest = itemRequestRepository.save(request);
        log.info("Запрос успешно добавлен с id: {}", createdItemRequest.getId());
        return ItemRequestMapper.toItemRequestDto(createdItemRequest);

    }

    @Override
    public List<ItemRequestResponseDto> findRequestByUserId(Long userId) {
        getUserById(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);
        log.info("Все запросы для пользователь с id: {} успешно найдены", userId);
        return ItemRequestMapper.toItemRequestResponseDtos(itemRequests);
    }

    @Override
    public List<ItemRequestDto> findAllWithParamsFromAndSize(Long userId, int from, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер списка должен быть больше 0");
        }
        if (from <= 0) {
            throw new IllegalArgumentException("Отсчет должен бначинаться не с 0");
        }
        getUserById(userId);
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(pageRequest);
        if (itemRequestPage.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("Запрашиваемая информация успешно найдена");
        return itemRequestPage.getContent().stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestResponseDto findItemRequestById(Long userId, Long requestId) {
        getUserById(userId);
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос с id: " + requestId + " не найден"));
        return ItemRequestMapper.toItemRequestResponseDto(request);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

}
