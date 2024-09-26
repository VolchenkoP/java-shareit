package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.config.constants.HttpHeaders;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_HEADER = HttpHeaders.USER_HEADER;
    private static final String STATE = HttpHeaders.DEFAULT_STATE;
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_HEADER) Long userId,
                                             @PathVariable Long bookingId) {
        log.info("Запрос данных бронирования по Id: {}", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByBookerId(@RequestHeader(USER_HEADER) Long userId,
                                                    @RequestParam(defaultValue = STATE) String state) {
        log.info("Запрос данных бронирования по забронировавшему пользователю с Id: {}", userId);
        BookingState stateValue = BookingState.parse(state);
        return bookingClient.findAllByBookerId(userId, stateValue);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader(USER_HEADER) Long userId,
                                                   @RequestParam(defaultValue = STATE) String state) {
        log.info("Запрос данных бронирования по владельцу объекта бронированияId: {}", userId);
        BookingState stateValue = BookingState.parse(state);
        return bookingClient.findAllByOwnerId(userId, stateValue);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addBooking(@RequestHeader(USER_HEADER) Long userId,
                                             @Valid @RequestBody BookingRequestDto bookingDto) {
        log.info("Добавление бронирования пользователем с Id: {}", userId);
        return bookingClient.addBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> bookingApproved(@RequestHeader(USER_HEADER) Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam boolean approved) {
        log.info("Подтверждение бронирования владельцем с Id: {}", userId);
        return bookingClient.bookingApproved(userId, bookingId, approved);
    }

}
