package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDTO userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDTO.builder()
                .id(1L)
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();
    }

    @Test
    @SneakyThrows
    void createUserCreatedSuccessfully() {
        when(userService.create(userDto)).thenReturn(userDto);
        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void updateUserUpdatedSuccessfully() {
        UserDTO userUpdateDto = UserDTO.builder()
                .name("Oleg Gazmanov Updated")
                .build();
        userDto.setName("Oleg Gazmanov Updated");
        when(userService.update(userDto.getId(), userUpdateDto)).thenReturn(userDto);
        String result = mockMvc.perform(patch("/users/{id}", userDto.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void getUserUserFound() {
        when(userService.findUserById(1L)).thenReturn(userDto);
        String result = mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
    }

    @Test
    @SneakyThrows
    void getAllUsersUsersFound() {
        when(userService.findAll()).thenReturn(List.of(userDto));
        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(userDto)), result);
    }

    @Test
    @SneakyThrows
    void deleteUserDeletedSuccessfully() {
        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(1L);
    }
}
