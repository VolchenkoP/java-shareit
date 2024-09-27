package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private static final String BOOKING_STATE_ALL = "ALL";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private UserDTO user;
    private BookingRequestDto bookingRequestDto;
    private BookingResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = UserDTO.builder()
                .id(1L)
                .name("John Doe")
                .email("j.doe@mail.ru")
                .build();

        ItemDTO itemDTO = ItemDTO.builder()
                .id(1L)
                .name("ItemName")
                .description("desc some")
                .available(true)
                .build();

        responseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.APPROVED)
                .item(itemDTO)
                .booker(user)
                .build();

        bookingRequestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

    }

    @Test
    @SneakyThrows
    void addBookingShouldReturnBookingResponseDto() {
        when(bookingService.addBooking(bookingRequestDto, user.getId())).thenReturn(responseDto);

        String result = mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(USER_HEADER, user.getId().toString())
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(responseDto), result);
    }

    @Test
    @SneakyThrows
    void approveBookingShouldReturnUpdatedBookingResponseDto() {
        long bookingId = 1L;
        boolean approved = true;

        when(bookingService.bookingUpdateStatus(user.getId(), bookingId, approved)).thenReturn(responseDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(USER_HEADER, user.getId().toString())
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(responseDto), result);
    }

    @Test
    @SneakyThrows
    void getBookingShouldReturnBookingResponseDto() {
        long bookingId = 1L;

        when(bookingService.getBooking(user.getId(), bookingId)).thenReturn(responseDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(USER_HEADER, user.getId().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(responseDto), result);
    }

    @Test
    @SneakyThrows
    void getBookingsShouldReturnListOfBookingShortResponseDto() {
        when(bookingService.findAllByBookerId(user.getId(), BOOKING_STATE_ALL))
                .thenReturn(List.of(responseDto));

        String result = mockMvc.perform(get("/bookings")
                        .param("state", BOOKING_STATE_ALL)
                        .header(USER_HEADER, user.getId().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(responseDto)), result);
    }

    @Test
    @SneakyThrows
    void getBookingsOwnerShouldReturnListOfBookingShortResponseDto() {
        when(bookingService.findAllByOwnerId(user.getId(), BOOKING_STATE_ALL))
                .thenReturn(List.of(responseDto));

        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", BOOKING_STATE_ALL)
                        .header(USER_HEADER, user.getId().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(responseDto)), result);
    }
}
