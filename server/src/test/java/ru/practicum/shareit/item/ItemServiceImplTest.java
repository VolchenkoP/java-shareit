package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@MockitoSettings(strictness = LENIENT)
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRequestRepository requestRepository;

    @Mock
    ItemMapper itemMapper;

    @InjectMocks
    ItemServiceImpl itemService;

    private Item item1, item2;
    private final User owner = User.builder()
            .id(1L)
            .name("Oleg Gazmanov")
            .email("vpole.skonem@viydu.ru")
            .build();

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(1L)
                .name("Item 1")
                .description("Item 1")
                .available(true)
                .owner(owner)
                .request(null)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("Item 2")
                .description("Item 2")
                .available(true)
                .owner(owner)
                .request(null)
                .build();
    }

    @Test
    void findItemsByTextShouldReturnSearchedItems() {
        when(itemRepository.searchText("text")).thenReturn(List.of(item1, item2));
        List<ItemDTO> items = itemService.findItemsByText("text");
        assertEquals(2, items.size());
        items = itemService.findItemsByText("");
        assertEquals(Collections.emptyList(), items);
    }

    @Test
    void addCommentToItemShouldThrowNotAvailableException() {
        User user = User.builder()
                .id(2L)
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();

        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        when(bookingRepository.findByItemIdAndBookerIdAndStatusAndEndBefore(anyLong(), anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        CommentRequestDto addDto = new CommentRequestDto();
        addDto.setText("text");

        assertThrows(ValidationException.class, () -> itemService.addComment(user.getId(), item1.getId(), addDto));
    }
}