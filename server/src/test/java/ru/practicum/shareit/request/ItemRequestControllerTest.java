package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.config.constants.HttpHeaders;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private static final String BASE_URL = "/requests";

    private final User user = User.builder()
            .id(1L)
            .name("Oleg Gazmanov")
            .email("vpole.skonem@viydu.ru")
            .build();

    private final ItemRequestDto requestDto = ItemRequestDto.builder()
            .id(1L)
            .description("Description")
            .requester(user)
            .created(LocalDateTime.now())
            .build();

    @Test
    @SneakyThrows
    void addItemRequestShouldReturnCreatedRequest() {
        ItemRequestDtoToAdd addDto = new ItemRequestDtoToAdd();
        addDto.setDescription("Description");
        when(itemRequestService.addItemRequest(user.getId(), addDto)).thenReturn(requestDto);
        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .header(HttpHeaders.USER_HEADER, String.valueOf(user.getId()))
                        .content(objectMapper.writeValueAsString(addDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
    }

    @Test
    @SneakyThrows
    void getItemRequestsShouldReturnListOfRequests() {
        ItemRequestResponseDto responseDto = ItemRequestResponseDto.builder()
                .description("Description")
                .created(LocalDateTime.now())
                .build();
        when(itemRequestService.findRequestByUserId(user.getId())).thenReturn(List.of(responseDto));
        mockMvc.perform(get(BASE_URL)
                        .header(HttpHeaders.USER_HEADER, String.valueOf(user.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(responseDto))));
    }

    @Test
    @SneakyThrows
    void getAllItemRequestsShouldReturnAllRequests() {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(2L)
                .description("Description")
                .requester(user)
                .created(LocalDateTime.now())
                .build();
        when(itemRequestService.findAllWithParamsFromAndSize(user.getId(), 0, 20)).thenReturn(List.of(dto));
        mockMvc.perform(get(BASE_URL + "/all")
                        .header(HttpHeaders.USER_HEADER, String.valueOf(user.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dto))));
    }

    @Test
    @SneakyThrows
    void getItemRequestShouldReturnSpecificRequest() {
        ItemRequestResponseDto responseDto = ItemRequestResponseDto.builder()
                .id(3L)
                .description("Description")
                .created(LocalDateTime.now())
                .build();
        when(itemRequestService.findItemRequestById(user.getId(), responseDto.getId())).thenReturn(responseDto);
        mockMvc.perform(get(BASE_URL + "/{requestId}", responseDto.getId())
                        .header(HttpHeaders.USER_HEADER, String.valueOf(user.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }
}
