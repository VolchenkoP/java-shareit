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
public class OwnerCurrentBookingHandler extends AbstractOwnerBookingsStateHandler {
    private final BookingRepository repository;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (request.getState() == BookingState.CURRENT) {
            return repository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    request.getUserId(), LocalDateTime.now(), LocalDateTime.now());
        }
        return super.handle(request);
    }
}
