package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingRequestDto {
    @NotNull
    private Long itemId;
    @FutureOrPresent(message = "Бронирвоание не может начаться в прошлом")
    @NotNull
    private LocalDateTime start;
    @Future(message = "Завершение бронирования должно быть в будущем")
    @NotNull
    private LocalDateTime end;

}
