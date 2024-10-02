package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDTO);

    UserDto findUserById(Long userDTOId);

    List<UserDto> findAll();

    UserDto update(Long userId, UserDto userDTO);

    void delete(Long userDTOId);

    void userExistById(Long userDTOId);

}
