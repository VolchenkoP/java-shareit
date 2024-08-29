package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User create(User user);

    User getUserById(Long userId);

    List<User> findAll();

    User update(User user);

    void delete(Long userId);
}
