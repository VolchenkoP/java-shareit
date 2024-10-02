package ru.practicum.shareit.booking.service.state.owner.handler;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

public interface OwnerBookingsStateHandler {
    List<Booking> handle(BookingRequest request);

    void setNext(OwnerBookingsStateHandler handler);
}
