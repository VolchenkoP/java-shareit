package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @GetMapping
    public List<UserDTO> getAll() {
        log.info("Получение списка всех пользователей");
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserDTOById(@PathVariable Long userId) {
        log.info("Получение данных пользователя с id: {}", userId);
        return service.findUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserDTO userDTO) {
        log.info("Создание нового пользователя");
        return service.create(userDTO);
    }

    @PatchMapping(path = "/{userId}")
    public UserDTO update(@PathVariable Long userId,
                          @RequestBody UserDTO userDTO) {
        log.info("Обновление данных пользователя с id: {}", userId);
        return service.update(userId, userDTO);
    }

    @DeleteMapping(path = "{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        service.deleteUserDTO(userId);
    }
}
