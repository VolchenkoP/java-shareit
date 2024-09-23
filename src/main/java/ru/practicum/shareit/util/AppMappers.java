package ru.practicum.shareit.util;

import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.mapper.UserMapper;

public final class AppMappers {
    public static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);


    private AppMappers() {

    }
}
