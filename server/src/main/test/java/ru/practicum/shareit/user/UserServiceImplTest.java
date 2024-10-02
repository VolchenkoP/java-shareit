package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final EntityManager em;
    private final UserService service;
    private final UserMapper mapper;

    @Test
    void getUserByIdTest() {
        UserDto userDto = makeUserDto("User", "some@email.com");
        UserDto savedUserDto = service.create(userDto);

        UserDto result = service.findUserById(savedUserDto.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(savedUserDto.getId()));
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void addUserTest() {
        UserDto userDto = makeUserDto("User", "some@email.com");
        service.create(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateEmailForUserTest() {
        UserDto userDto = makeUserDto("User", "some@email.com");
        UserDto savedUserDto = service.create(userDto);
        String newEmail = "another@email.com";
        UserDto userUpdateDto = UserDto.builder()
                .email(newEmail)
                .name("User")
                .build();

        service.update(savedUserDto.getId(), userUpdateDto);
        UserDto result = service.findUserById(savedUserDto.getId());

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(savedUserDto.getId()));
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(newEmail));
    }

    @Test
    void deleteUserTest() {
        UserDto userDto = makeUserDto("User", "some@email.com");
        UserDto savedUserDto = service.create(userDto);
        service.delete(savedUserDto.getId());

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);

        Assertions.assertThrows(NoResultException.class, () -> query.setParameter("email", userDto.getEmail())
                .getSingleResult());
    }

    @Test
    void throwExceptionWhenGetUserByIdTest() {
        Assertions.assertThrows(NotFoundException.class, () -> service.findUserById(999999L));
    }

    @Test
    void returnTrueForExistedUserTest() {
        UserDto userDto = makeUserDto("User", "some@email.com");
        UserDto savedUserDto = service.create(userDto);

        assertNotNull(savedUserDto);
    }

    private UserDto makeUserDto(String name, String email) {
        UserDto userDto = new UserDto();
        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;
    }
}