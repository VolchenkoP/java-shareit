package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(UserDTO userDTO);

    UserDTO findUserById(Long userDTOId);

    List<UserDTO> findAll();

    UserDTO update(Long userId, UserDTO userDTO);

    void delete(Long userDTOId);

    void userExistById(Long userDTOId);

}
