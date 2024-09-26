package ru.practicum.shareit.booking.service.state.booker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerAllBookingsHandlerBooker;
import ru.practicum.shareit.booking.service.state.booker.handler.BookerBookingStateHandler;
import ru.practicum.shareit.booking.service.state.booker.handler.CurrentBookingsHandlerBooker;
import ru.practicum.shareit.booking.service.state.booker.handler.FutureBookingsHandlerBooker;
import ru.practicum.shareit.booking.service.state.booker.handler.PastBookingsHandlerBooker;
import ru.practicum.shareit.booking.service.state.booker.handler.RejectedBookingsHandlerBooker;
import ru.practicum.shareit.booking.service.state.booker.handler.WaitingBookingsHandlerBooker;

import java.util.List;

@Component
public class BookerHandlerChain {
    private final BookerBookingStateHandler handler;

    @Autowired
    public BookerHandlerChain(BookerAllBookingsHandlerBooker allHandler,
                              PastBookingsHandlerBooker pastHandler,
                              CurrentBookingsHandlerBooker currentHandler,
                              FutureBookingsHandlerBooker futureHandler,
                              WaitingBookingsHandlerBooker waitingHandler,
                              RejectedBookingsHandlerBooker rejectedHandler) {
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
