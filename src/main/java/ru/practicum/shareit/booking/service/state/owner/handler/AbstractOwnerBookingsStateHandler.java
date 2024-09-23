package ru.practicum.shareit.booking.service.state.owner.handler;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.Collections;
import java.util.List;

public abstract class AbstractOwnerBookingsStateHandler implements OwnerBookingsStateHandler {
    protected OwnerBookingsStateHandler handler;

    @Override
    public List<Booking> handle(BookingRequest request) {
        if (handler != null) {
            return handler.handle(request);
        }
        return Collections.emptyList();
    }

    @Override
    public void setNext(OwnerBookingsStateHandler handler) {
        this.handler = handler;
    }
}
