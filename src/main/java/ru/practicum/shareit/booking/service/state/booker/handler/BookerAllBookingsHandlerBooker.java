package ru.practicum.shareit.booking.service.state.booker.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookerAllBookingsHandlerBooker extends AbstractBookerBookerBookingStateHandler {
    private final BookingRepository repository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.ALL) {
            return repository.findAllByBookerIdOrderByStartDesc(request.getUserId());
        }
        return super.handle(request);
    }
}
