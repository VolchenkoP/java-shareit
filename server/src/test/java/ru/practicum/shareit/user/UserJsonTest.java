package ru.practicum.shareit.user;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserJsonTest {

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "Oleg Gazmanov";
    private static final String USER_EMAIL = "vpole.skonem@viydu.ru";
    @Autowired
    private JacksonTester<UserDTO> json;

    @Test
    @SneakyThrows
    void userDtoSerialize() {
        UserDTO userDto = UserDTO.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

        JsonContent<UserDTO> content = json.write(userDto);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.email")
                .doesNotHaveJsonPath("$.unexpectedField");


        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(USER_ID.intValue());
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo(USER_NAME);
        assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo(USER_EMAIL);
    }


    @Test
    @SneakyThrows
    void userDtoSerializeEmptyFields() {
        UserDTO userDto = UserDTO.builder()
                .id(null)
                .name("")
                .email("")
                .build();

        JsonContent<UserDTO> content = json.write(userDto);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.email");

        assertThat(content).extractingJsonPathValue("$.id").isNull();
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo("");
        assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo("");
    }

    @Test
    @SneakyThrows
    void userDtoDeserialize() {
        String jsonContent = "{\"id\":1,\"name\":\"Oleg Gazmanov\",\"email\":\"vpole.skonem@viydu.ru\"}";
        UserDTO userDto = json.parseObject(jsonContent);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(USER_ID);
        assertThat(userDto.getName()).isEqualTo(USER_NAME);
        assertThat(userDto.getEmail()).isEqualTo(USER_EMAIL);
    }


}
