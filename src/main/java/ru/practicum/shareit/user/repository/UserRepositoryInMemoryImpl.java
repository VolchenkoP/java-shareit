package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long usersId = 0L;

    @Override
    public User create(User user) {
        user.setId(++usersId);
        users.put(usersId, user);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user) {
        User updatedUser = users.get(user.getId());
        users.put(updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }
}
