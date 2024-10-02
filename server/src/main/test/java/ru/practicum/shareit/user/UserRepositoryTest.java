package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    @Autowired
    private final UserRepository userRepository;
    private final User testUser = User.builder()
            .name("John Doe")
            .email("john@doe.ru")
            .build();
    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        testEntityManager.persist(testUser);
        testEntityManager.flush();
    }

    @AfterEach
    void tearDown() {
        testEntityManager.remove(testUser);
    }

    @Test
    void whenFindByEmailThenReturnUser() {
        Optional<User> foundUser = userRepository.findByEmail("john@doe.ru");
        assertEquals(testUser, foundUser.orElse(null));
    }
}
