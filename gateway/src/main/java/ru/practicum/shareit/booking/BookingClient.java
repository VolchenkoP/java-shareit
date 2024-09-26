package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.config.constants.ApiConstants;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = ApiConstants.BOOKING_API_PREFIX;

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(BookingRequestDto bookingDto, Long userId) {
        return post("", userId, bookingDto);
    }

    public ResponseEntity<Object> bookingApproved(Long userId, Long bookingId, Boolean approved) {
        return patch("/" + bookingId + "?approved={approved}",
                userId, Map.of("approved", approved), null);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> findAllByBookerId(Long userId, BookingState state) {
        return get("?state={state}", userId, Map.of("state", String.valueOf(state)));
    }

    public ResponseEntity<Object> findAllByOwnerId(Long userId, BookingState state) {
        return get("/owner?state={state}", userId, Map.of("state", String.valueOf(state)));
    }
}
