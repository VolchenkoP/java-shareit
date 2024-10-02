package ru.practicum.shareit.exeption;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ErrorHandlerTest {
    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleNotFound() {
        var ex = new NotFoundException("Object not found!");
        var result = errorHandler.handleNotFoundException(ex);
        assertNotNull(result);
        assertEquals("Данные не найдены", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }

    @Test
    void handleValidation() {
        var ex = new ValidationException("Validation error occurred!");
        var result = errorHandler.handleValidationException(ex);
        assertNotNull(result);
        assertEquals("Данные не прошли валидацию", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }

    @Test
    void handleRuntimeException() {
        var ex = new RuntimeException("Runtime error occurred!");
        var result = errorHandler.handleRuntimeException(ex);
        assertNotNull(result);
        assertEquals("Внутренняя ошибка приложения", result.getError());
        assertEquals(ex.getMessage(), result.getDescription());
    }

}
