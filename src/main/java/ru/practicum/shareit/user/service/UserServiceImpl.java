package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDTO create(UserDTO userDTO) {
        log.info("Создание нового пользователя");
        repository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            throw new RuntimeException("Пользователь с email: " + userDTO.getEmail() + "уже существует");
        });
        User savedUser = repository.save(UserMapper.toEntity(userDTO));
        log.info("Новый пользователь успешно создан с id: {}", savedUser.getId());
        return UserMapper.toDTO(savedUser);
    }

    @Override
    public UserDTO findUserById(Long userDTOId) {
        log.info("Поиск пользователя с Id: {}", userDTOId);
        userExistById(userDTOId);
        log.info("Пользователь с  id: {} успешно найден", userDTOId);
        return UserMapper.toDTO(repository.findById(userDTOId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userDTOId + " не найден")));
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Поиск всех пользователей");
        return repository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public UserDTO update(Long userId, UserDTO userDTO) {
        log.info("Обновление данных у пользователя с Id: {}", userId);
        userExistById(userId);
        User user = UserMapper.toEntity(userDTO);
        User updatedUser = repository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id: " + userId + " не найден"));
        repository.findByEmail(user.getEmail()).ifPresent(
                u -> {
                    if (!updatedUser.getEmail().equals(u.getEmail())) {
                        throw new RuntimeException("Пользователь с email: " + userDTO.getEmail() + "уже существует");
                    }
                });
        if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail())) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isEmpty()) {
            updatedUser.setName(user.getName());
        }
        log.info("Обновление пользователя с Id: {} прошло успешно", userId);
        return UserMapper.toDTO(repository.save(updatedUser));

    }

    @Override
    @Transactional
    public void deleteUserDTO(Long userDTOId) {
        log.info("Удаление пользователя с Id: {}", userDTOId);
        userExistById(userDTOId);
        repository.deleteById(userDTOId);
        log.info("Пользователь с Id: {} удален", userDTOId);
    }

    @Override
    public void userExistById(Long userDTOId) {
        log.info("Проверка на существовоание пользователя с Id: {}", userDTOId);
        if (!repository.existsById(userDTOId)) {
            log.error("Пользователя с Id: {} не существует", userDTOId);
            throw new NotFoundException("Пользователь с Id: " + userDTOId + " не найден");
        }
    }

}
