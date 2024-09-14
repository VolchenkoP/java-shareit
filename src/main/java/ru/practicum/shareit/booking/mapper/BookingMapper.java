package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

@UtilityClass
public class BookingMapper {

    public BookingResponseDto toDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapperImpl.toDTO(booking.getItem()))
                .booker(UserMapper.toDTO(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public Booking toEntity(BookingRequestDto bookingRequestDto) {
        Item item = new Item();
        item.setId(bookingRequestDto.getItemId());
        return Booking.builder()
                .start(bookingRequestDto.getStart())
                .end(bookingRequestDto.getEnd())
                .item(item)
                .build();
    }

}
