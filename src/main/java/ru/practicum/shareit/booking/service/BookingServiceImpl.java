package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.booker.BookerHandlerChain;
import ru.practicum.shareit.booking.service.state.owner.OwnerHandlerChain;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    private final BookerHandlerChain bookerChain;
    private final OwnerHandlerChain ownerChain;


    @Override
    @Transactional
    public BookingResponseDto addBooking(BookingRequestDto bookingDto, Long userId) {
        log.info("Запрос на новое бронирование объекта пользователем с id: {}", userId);
        User booker = getUserById(userId);
        Item item = getItemById(bookingDto.getItemId());
        availableValidation(item, booker);

        Booking booking = BookingMapper.toEntity(bookingDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("Запрос на бронирование создан с id: {}", savedBooking.getId());
        return BookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional
    public BookingResponseDto bookingUpdateStatus(Long userId, Long bookingId, boolean approve) {
        log.info("Запрос на подтверждение бронирования с id: {}", bookingId);
        Booking booking = getBookingById(bookingId);
        ownerCheck(userId, booking);
        updateStatus(booking, approve);
        log.info("Бронирование подтверждено");
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        log.info("Получение данных бронирования с id: {} пользователем с id: {}", bookingId, userId);
        Booking booking = getBookingById(bookingId);
        accessCheck(userId, booking);
        log.info("Данные доступны и направлены");
        return BookingMapper.toDto(booking);
    }


    @Override
    public List<BookingResponseDto> findAllByBookerId(Long userId, String state) {
        log.info("Получение данных всех бронирований у пользователя с id: {}", userId);
        getUserById(userId);
        BookingState bookingState = getBookingStateFromRequest(state);
        List<Booking> bookings = getResultByBookerHandlerChain(userId, bookingState);
        log.info("Данные всех бронирований пользователя доступны и направлены");
        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> findAllByOwnerId(Long userId, String state) {
        log.info("Получение данных всех объектов бронированя у пользователя с id: {}", userId);
        getUserById(userId);
        BookingState bookingState = getBookingStateFromRequest(state);
        List<Booking> bookings = getResultByOwnerHandlerChain(userId, bookingState);
        log.info("Данные всех объектов бронирования пользователя доступны и направлены");
        return bookings.stream()
                .map(BookingMapper::toDto)
                .toList();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));
    }

    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() ->
                new NotFoundException("Объект проката с id: " + itemId + " не найден"));
    }

    private Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронрование с id: " + bookingId + " не найден"));
    }

    private void availableValidation(Item item, User booker) {
        if (!item.getAvailable()) {
            log.error("Объект бронирования с id: {} не доступен", item.getId());
            throw new ValidationException("Объект бронирвоания с id: " + item.getId() + " не доступен");
        }
        if (booker.getId().equals(item.getOwner().getId())) {
            log.error("Попытка бронирования своего объекта бронирования");
            throw new NotFoundException("Невозможно бронировать свой объект бронирования");
        }
    }

    private void ownerCheck(Long userId, Booking booking) {
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            log.error("Обращение к данным бронирования пользователем, не являющимся владельцем");
            throw new ValidationException("У пользователя нет доступа к данным чужого бронирвоания");
        }
    }

    private void accessCheck(Long userId, Booking booking) {
        if (!booking.getItem().getOwner().getId().equals(userId) &&
                !booking.getBooker().getId().equals(userId)) {
            log.error("Обращение к данным бронирования пользователем, не осуществляющим бронь");
            throw new NotFoundException("У пользователя нет доступа к данным чужого бронирвоания");
        }
    }

    private void updateStatus(Booking booking, boolean approve) {
        if (booking.getStatus() == BookingStatus.APPROVED) {
            log.error("Попытка дублирования одобрения бронирования");
            throw new ValidationException("Статус уже одобрен");
        }
        booking.setStatus(approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);
    }

    private BookingState getBookingStateFromRequest(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Неопознаное состояние бронирвоания: " + state);
        }
    }

    private List<Booking> getResultByBookerHandlerChain(Long userId, BookingState state) {
        BookingRequest request = new BookingRequest(userId, state);
        return bookerChain.handle(request);
    }

    private List<Booking> getResultByOwnerHandlerChain(Long userId, BookingState state) {
        BookingRequest request = new BookingRequest(userId, state);
        return ownerChain.handle(request);
    }

}
