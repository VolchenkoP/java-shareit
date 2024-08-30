package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User fromDTO(UserDTO userDTO);
}
