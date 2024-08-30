package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final Map<Long, String> emails = new HashMap<>();
    private final UserMapperImpl mapper;

    @Override
    public UserDTO create(UserDTO userDTO) {
        log.info("Создание нового пользователя");
        User user = mapper.fromDTO(userDTO);
        userEmailNotDuplicate(userDTO);
        UserDTO createdUser = mapper.toDTO(repository.create(user));
        emails.put(createdUser.getId(), createdUser.getEmail());
        return createdUser;
    }

    @Override
    public UserDTO findUserById(Long userDTOId) {
        log.info("Поиск пользователя с Id: {}", userDTOId);
        userExistById(userDTOId);
        return mapper.toDTO(repository.getUserById(userDTOId));
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Поиск всех пользователей");
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO update(Long userId, UserDTO userDTO) {
        log.info("Обновление данных у пользователя с Id: {}", userId);
        userExistById(userId);
        User updateUser = repository.getUserById(userId);
        if (userDTO.getEmail() != null) {
            if (!updateUser.getEmail().equals(userDTO.getEmail())) {
                userEmailNotDuplicate(userDTO);
                updateUser.setEmail(userDTO.getEmail());
                emails.put(userId, userDTO.getEmail());
            }
        }
        if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
            updateUser.setName(userDTO.getName());
        }
        log.info("Обновление пользователя с Id: {} прошло успешно", userId);
        return mapper.toDTO(repository.update(updateUser));

    }

    @Override
    public void deleteUserDTO(Long userDTOId) {
        log.info("Удаление пользователя с Id: {}", userDTOId);
        userExistById(userDTOId);
        repository.delete(userDTOId);
        log.info("Пользователь с Id: {} удален", userDTOId);
    }

    @Override
    public void userExistById(Long userDTOId) {
        log.info("Проверка на существовоание пользователя с Id: {}", userDTOId);
        if (repository.findAll().stream()
                .noneMatch(user -> user.getId().equals(userDTOId))) {
            log.error("Пользователя с Id: {} не существует", userDTOId);
            throw new NotFoundException("Пользователь с Id: " + userDTOId + " не найден");
        }
    }

    private void userEmailNotDuplicate(UserDTO userDTO) {
        log.info("Проверка на дублирование существующего email у пользователя с email: {}", userDTO.getEmail());
        if (emails.containsValue(userDTO.getEmail())) {
            log.error("Дублирование email: {}", userDTO.getEmail());
            throw new RuntimeException("Пользователь с таким email: " + userDTO.getEmail() + " уже зарегистрирован");
        }
    }
}
