package ru.practicum.shareit.booking.service.state.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerAllBookingsHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerBookingsStateHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerCurrentBookingHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerFutureBookingsHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerPastBookingsHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerRejectedBookingsHandler;
import ru.practicum.shareit.booking.service.state.owner.handler.OwnerWaitingBookingsHandler;

import java.util.List;

@Component
public class OwnerHandlerChain {
    private final OwnerBookingsStateHandler handler;

    @Autowired
    public OwnerHandlerChain(OwnerAllBookingsHandler allHandler,
                             OwnerPastBookingsHandler pastHandler,
                             OwnerCurrentBookingHandler currentHandler,
                             OwnerFutureBookingsHandler futureHandler,
                             OwnerWaitingBookingsHandler waitingHandler,
                             OwnerRejectedBookingsHandler rejectedHandler) {
        allHandler.setNext(pastHandler);
        pastHandler.setNext(currentHandler);
        currentHandler.setNext(futureHandler);
        futureHandler.setNext(waitingHandler);
        waitingHandler.setNext(rejectedHandler);

        this.handler = allHandler;
    }

    public List<Booking> handle(BookingRequest request) {
        return handler.handle(request);
    }
}
