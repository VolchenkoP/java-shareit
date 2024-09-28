package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemJsonTest {
    private final User user = User.builder()
            .id(1L)
            .name("Oleg Gazmanov")
            .email("vpole.skonem@viydu.ru")
            .build();
    @Autowired
    private JacksonTester<ItemDTO> json;

    @Test
    @SneakyThrows
    void serializeItemDtocorrectlySerialized() {
        ItemDTO itemDto = ItemDTO.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .request(null)
                .build();

        JsonContent<ItemDTO> content = json.write(itemDto);

        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.request");

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(content).extractingJsonPathStringValue("$.description").isEqualTo("Description");
        assertThat(content).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(content).extractingJsonPathStringValue("$.request").isNull();
    }

    @Test
    @SneakyThrows
    void deserializeItemDtocorrectlyDeserialized() {
        String jsonString = "{\"id\":1,\"name\":\"Item\",\"description\":\"Description\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"Oleg Gazmanov\",\"email\":\"vpole.skonem@viydu.ru\"},\"request\":null}";

        ItemDTO itemDto = json.parseObject(jsonString);

        assertThat(itemDto).isNotNull();
        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Item");
        assertThat(itemDto.getDescription()).isEqualTo("Description");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getRequest()).isNull();
    }
}
