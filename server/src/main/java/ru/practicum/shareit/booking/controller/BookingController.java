package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.config.constants.HttpHeaders;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_HEADER = HttpHeaders.USER_HEADER;
    private static final String STATE = HttpHeaders.DEFAULT_STATE;
    private final BookingService service;

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto getBooking(@RequestHeader(USER_HEADER) Long userId,
                                         @PathVariable Long bookingId) {
        log.info("Запрос данных бронирования по Id: {}", bookingId);
        return service.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> findAllByBookerId(@RequestHeader(USER_HEADER) Long userId,
                                                      @RequestParam(defaultValue = STATE) String state) {
        log.info("Запрос данных бронирования по забронировавшему пользователю с Id: {}", userId);
        return service.findAllByBookerId(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> findAllByOwnerId(@RequestHeader(USER_HEADER) Long userId,
                                                     @RequestParam(defaultValue = STATE) String state) {
        log.info("Запрос данных бронирования по владельцу объекта бронированияId: {}", userId);
        return service.findAllByOwnerId(userId, state);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto addBooking(@RequestHeader(USER_HEADER) Long userId,
                                         @Valid @RequestBody BookingRequestDto bookingDto) {
        log.info("Добавление бронирования пользователем с Id: {}", userId);
        return service.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingResponseDto bookingUpdateStatus(@RequestHeader(USER_HEADER) Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam boolean approved) {
        log.info("Подтверждение бронирования владельцем с Id: {}", userId);
        return service.bookingUpdateStatus(userId, bookingId, approved);
    }

}
