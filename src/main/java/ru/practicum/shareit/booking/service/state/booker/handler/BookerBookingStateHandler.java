package ru.practicum.shareit.booking.service.state.booker.handler;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;

import java.util.List;

public interface BookerBookingStateHandler {
    List<Booking> handle(BookingRequest request);

    void setNext(BookerBookingStateHandler nextHandler);
}
