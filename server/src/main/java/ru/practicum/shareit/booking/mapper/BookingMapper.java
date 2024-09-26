package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface BookingMapper {

    @Mapping(target = "item", source = "booking.item")
    @Mapping(target = "booker", source = "booking.booker")
    BookingResponseDto toDto(Booking booking);

    @Mapping(target = "item.id", source = "dto.itemId")
    Booking toEntity(BookingRequestDto dto);

    /*
    private static final UserMapper USER_MAPPER = AppMappers.USER_MAPPER;

    public BookingResponseDto toDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toDTO(booking.getItem()))
                .booker(USER_MAPPER.toDTO(booking.getBooker()))
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
    } */

}
