package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(BookingRequestDto bookingDto, Long userId);

    BookingResponseDto bookingUpdateStatus(Long userId, Long bookingId, boolean approve);

    BookingResponseDto getBooking(Long userId, Long bookingId);

    List<BookingResponseDto> findAllByBookerId(Long userId, String state);

    List<BookingResponseDto> findAllByOwnerId(Long userId, String state);

}
