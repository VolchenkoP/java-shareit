package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemDTO create(Long userId, ItemDTO itemDTO) {
        log.info("Создание нового объекта проката пользователем с Id: {}", userId);
        User owner = userValidation(userId);
        Item item = ItemMapperImpl.fromDTO(itemDTO);
        item.setOwner(owner);
        log.info("Создание нового объекта проката прошло успешно, id объекта: {}", item.getId());
        return ItemMapperImpl.toDTO(repository.save(item));
    }

    @Override
    @Transactional
    public ItemDTO update(Long userId, Long itemId, ItemFromUpdateRequestDto itemDTO) {
        log.info("Обновление данных по объекту проката с Id: {}", itemId);
        userValidation(userId);
        isOwner(userId, itemId);
        Item updatedItem = itemValidation(itemId);
        Item itemFromRequest = ItemMapperImpl.fromUpdateDto(itemDTO);
        updatedItem.setId(itemId);
        if (itemFromRequest.getName() != null) {
            updatedItem.setName(itemFromRequest.getName());
        }
        if (itemFromRequest.getDescription() != null) {
            updatedItem.setDescription(itemFromRequest.getDescription());
        }
        if (itemFromRequest.getAvailable() != null) {
            updatedItem.setAvailable(itemFromRequest.getAvailable());
        }
        log.info("Обновление данных по объекту проката с Id: {} прошло успешно", itemId);
        return ItemMapperImpl.toDTO(repository.save(updatedItem));
    }

    @Override
    public ExtendedItemDto findItemByItemId(Long itemId, Long userId) {
        log.info("Поиск объекта проката с Id: {}", itemId);
        userValidation(userId);
        Item item = itemValidation(itemId);
        List<Booking> bookings = bookingRepository.findAllByItemIdAndItemOwnerIdAndStatusNotIn(
                itemId, userId, List.of(BookingStatus.REJECTED));
        return ItemMapperImpl.toExtendedItemDto(item, bookings);
    }

    @Override
    public List<ExtendedItemDto> findItemsByUserId(Long userId) {
        log.info("Поиск объектов проката по пользователю с Id: {}", userId);
        userService.userExistById(userId);
        List<Item> items = repository.findItemsByUserId(userId);
        List<Booking> bookings = bookingRepository.findAllByItemInAndStatusNotIn(
                items, List.of(BookingStatus.REJECTED));
        return ItemMapperImpl.toListExtendedItemDto(items, bookings);
    }

    @Override
    public List<ItemDTO> findItemsByText(String text) {
        log.info("Поиск объекта проката по совпадению с поисковой строкой {}", text);
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return repository.findItemByText(text).stream()
                .map(ItemMapperImpl::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentRequestDto) {
        Item item = itemValidation(itemId);
        User author = userValidation(userId);


        List<Booking> bookings = bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (CollectionUtils.isEmpty(bookings)) {
            throw new ValidationException("Пользователь с id: " + userId
                    + " не бронировал объект проката с id: " + itemId);
        }

        Comment comment = CommentMapper.toEntity(commentRequestDto);
        comment.setItem(item);
        comment.setAuthor(author);

        return CommentMapper.toCommentResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long itemId) {
        log.info("Удаление объекта проката с Id: {}", itemId);
        isItemExistByItemId(itemId);
        repository.deleteById(itemId);
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
        if (!repository.findById(itemId).orElseThrow(() ->
                        new NotFoundException("Объект проката с id: " + itemId + " не найден"))
                .getOwner().getId().equals(userId)) {
            log.error("Пользователь с Id: {} не является создателем Объекта проката с Id: {}", userId, itemId);
            throw new ValidationException("Пользователь с Id: " + userId
                    + " не является создателем объекта проката с Id: " + itemId);
        }
    }

    private Item getItemById(Long itemId) {
        return repository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Объект проката с id: " + itemId + " не найден"));
    }

    private User userValidation(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
    }

    private Item itemValidation(Long itemId) {
        return repository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Объект проката с id: " + itemId + " не найден"));
    }
}
