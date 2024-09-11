package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

@Component
@Mapper(componentModel = "Spring")
public class UserMapperImpl implements UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User fromDTO(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();
    }
}
