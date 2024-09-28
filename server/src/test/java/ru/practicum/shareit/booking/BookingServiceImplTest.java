package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.service.state.BookingRequest;
import ru.practicum.shareit.booking.service.state.booker.BookerHandlerChain;
import ru.practicum.shareit.booking.service.state.owner.OwnerHandlerChain;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private OwnerHandlerChain ownerHandlerChain;

    @Mock
    private BookerHandlerChain bookerHandlerChain;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User testBooker;
    private User testOwner;
    private Item testItem;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testBooker = User.builder()
                .id(1L)
                .name("Jane Doe")
                .email("j.doe@example.com")
                .build();

        testOwner = User.builder()
                .id(2L)
                .name("John Doe")
                .email("John.d@example.com")
                .build();

        testItem = Item.builder()
                .id(1L)
                .name("Item")
                .description("Desc1")
                .available(true)
                .owner(testOwner)
                .build();

        testBooking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status(BookingStatus.APPROVED)
                .item(testItem)
                .booker(testBooker)
                .build();
    }

    @Test
    void getBookingsByBookerShouldReturnListOfBookingShortResponseDto() {
        when(userRepository.findById(testBooker.getId())).thenReturn(Optional.of(testBooker));
        when(bookerHandlerChain.handle(any(BookingRequest.class))).thenReturn(List.of(testBooking));


        List<BookingResponseDto> all = bookingService.findAllByBookerId(testBooker.getId(), "ALL");
        assertEquals(1, all.size());

        List<BookingResponseDto> current = bookingService.findAllByBookerId(testBooker.getId(), "CURRENT");
        assertEquals(1, current.size());
    }

    @Test
    void getBookingsByOwnerShouldReturnListOfBookingShortResponseDto() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testOwner));

        when(ownerHandlerChain.handle(any(BookingRequest.class))).thenReturn(List.of(testBooking));

        List<BookingResponseDto> responseList = bookingService.findAllByOwnerId(testOwner.getId(), "ALL");

        assertEquals(1, responseList.size());
    }


}
