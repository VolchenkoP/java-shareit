package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.config.constants.HttpHeaders;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private User owner;
    private ItemDTO itemDto;
    private ExtendedItemDto itemComment;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("Oleg Gazmanov")
                .email("vpole.skonem@viydu.ru")
                .build();

        itemDto = ItemDTO.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .build();

        itemComment = ExtendedItemDto.builder()
                .id(2L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .comments(null)
                .build();
    }

    @Test
    @SneakyThrows
    void createItemWhatShouldBeReturned() {
        ItemCreateDto itemAddDto = ItemCreateDto.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .build();
        when(itemService.create(1L, itemAddDto)).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_HEADER, "1")
                        .content(objectMapper.writeValueAsString(itemAddDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    @SneakyThrows
    void updateItemwhatShouldBeReturned() {
        ItemFromUpdateRequestDto itemUpdateDto = ItemFromUpdateRequestDto.builder()
                .name("Item1")
                .description("Item1")
                .build();
        when(itemService.update(1L, 1L, itemUpdateDto)).thenReturn(itemDto);

        String result = mockMvc.perform(patch("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto))
                        .header(HttpHeaders.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @Test
    @SneakyThrows
    void getAllItemswhatShouldBeReturned() {
        when(itemService.findItemsByUser(1L)).thenReturn(List.of(itemComment));

        String result = mockMvc.perform(get("/items")
                        .header(HttpHeaders.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemComment)), result);
    }

    @Test
    @SneakyThrows
    void getItemByIdwhatShouldBeReturned() {
        when(itemService.findItemById(1L, 1L)).thenReturn(itemComment);

        String result = mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(HttpHeaders.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemComment), result);
    }

    @Test
    @SneakyThrows
    void searchItemsByTextwhatShouldBeReturned() {
        when(itemService.findItemsByText(anyString())).thenReturn(List.of(itemDto));

        String result = mockMvc.perform(get("/items/search")
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @Test
    @SneakyThrows
    void addCommentToItemWhatShouldBeReturned() {
        CommentRequestDto commentAddDto = new CommentRequestDto();
        commentAddDto.setText("comment");
        CommentResponseDto responseDto = CommentResponseDto.builder()
                .authorName("John Doe")
                .text("comment")
                .created(LocalDateTime.now())
                .build();
        when(itemService.addComment(1L, 1L, commentAddDto)).thenReturn(responseDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_HEADER, "1")
                        .content(objectMapper.writeValueAsString(commentAddDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(responseDto), result);
    }

    @Test
    @SneakyThrows
    void createItemWhenErrorOccurs_whatShouldBeReturned() {
        ItemCreateDto itemAddDto = ItemCreateDto.builder()
                .id(1L)
                .name("Item1")
                .description("Item1")
                .available(true)
                .owner(owner)
                .build();
        when(itemService.create(1L, itemAddDto)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.USER_HEADER, "1")
                        .content(objectMapper.writeValueAsString(itemAddDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createItem_ShouldReturnItem() throws Exception {
        when(itemService.create(anyLong(), any(ItemCreateDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"name\": \"Test item\", \"description\": \"Test description\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemDto.getId())); // замените на реальные проверки

        verify(itemService, times(1)).create(anyLong(), any(ItemCreateDto.class));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() throws Exception {

        when(itemService.update(anyLong(), anyLong(), any(ItemFromUpdateRequestDto.class))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content("{\"name\": \"Updated item\", \"description\": \"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));

        verify(itemService, times(1)).update(anyLong(), anyLong(), any(ItemFromUpdateRequestDto.class));
    }

}
