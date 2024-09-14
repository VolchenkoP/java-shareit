package ru.practicum.shareit.booking.service.state.owner.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OwnerFutureBookingsHandler extends AbstractOwnerBookingsStateHandler {
    private final BookingRepository repository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.FUTURE) {
            return repository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                    request.getUserId(), LocalDateTime.now());
        }
        return super.handle(request);
    }
}
