package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {
    private final EntityManager em;
    private final ItemRequestService service;
    private final UserService userService;

    @Test
    @DisplayName(value = "Добавление запроса на позицию")
    void addItemRequestTest() {
        ItemRequestDtoToAdd itemRequestDto = new ItemRequestDtoToAdd();
        itemRequestDto.setDescription("desc");
        UserDto userDto = userService.create(UserDto.builder()
                .name("user")
                .email("user@mail.ru")
                .build());
        ItemRequestDto result = service.addItemRequest(userDto.getId(), itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select u from ItemRequest u where u.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", result.getId())
                .getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getRequester().getId(), equalTo(userDto.getId()));
    }

    @Test
    @DisplayName(value = "Получение всех запросов")
    void getAllItemRequestsTest() {
        UserDto userDto = userService.create(UserDto.builder()
                .name("user")
                .email("user@mail.ru")
                .build());
        ItemRequestDtoToAdd itemRequestDto = new ItemRequestDtoToAdd();
        itemRequestDto.setDescription("new item request");
        ItemRequestDtoToAdd itemRequestDtoAnother = new ItemRequestDtoToAdd();
        itemRequestDtoAnother.setDescription("another item request");
        ItemRequestDtoToAdd itemRequestDtoThird = new ItemRequestDtoToAdd();
        itemRequestDtoThird.setDescription("third item request");
        service.addItemRequest(userDto.getId(), itemRequestDto);
        service.addItemRequest(userDto.getId(), itemRequestDtoAnother);
        service.addItemRequest(userDto.getId(), itemRequestDtoThird);

        Collection<ItemRequestResponseDto> result = service.findRequestByUserId(userDto.getId());
        assertThat(result, hasSize(3));
    }

    @Test
    @DisplayName(value = "Получение запросов по ид пользователя")
    void getItemRequestsByRequestorIdTest() {
        UserDto userDto = userService.create(UserDto.builder()
                .name("user")
                .email("user@mail.ru")
                .build());
        UserDto userDtoAnother = userService.create(UserDto.builder()
                .name("user2")
                .email("user2@mail.ru")
                .build());
        ItemRequestDtoToAdd itemRequestDto = new ItemRequestDtoToAdd();
        itemRequestDto.setDescription("new item request");
        ItemRequestDtoToAdd itemRequestDtoAnother = new ItemRequestDtoToAdd();
        itemRequestDtoAnother.setDescription("another item request");

        service.addItemRequest(userDto.getId(), itemRequestDto);
        service.addItemRequest(userDtoAnother.getId(), itemRequestDtoAnother);

        List<ItemRequestResponseDto> result = service.findRequestByUserId(userDto.getId());
        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getDescription(), is(itemRequestDto.getDescription()));
        List<ItemRequestResponseDto> resultAnother = service.findRequestByUserId(userDtoAnother.getId());
        assertThat(resultAnother, hasSize(1));
        assertThat(resultAnother.getFirst().getDescription(), is(itemRequestDtoAnother.getDescription()));
    }

    @Test
    @DisplayName(value = "Получение отдельного запроса ИД")
    void getItemRequestByIdTest() {
        UserDto userDto = userService.create(UserDto.builder()
                .name("user")
                .email("user@mail.ru")
                .build());
        ItemRequestDtoToAdd itemRequestDto = new ItemRequestDtoToAdd();
        itemRequestDto.setDescription("new item request");

        ItemRequestDto addedItemRequest = service.addItemRequest(userDto.getId(), itemRequestDto);
        ItemRequestResponseDto result = service.findItemRequestById(userDto.getId(), addedItemRequest.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(result.getId(), equalTo(addedItemRequest.getId()));
    }

}
