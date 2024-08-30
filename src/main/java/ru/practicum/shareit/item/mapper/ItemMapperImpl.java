package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

@Component
@Mapper(componentModel = "spring")
public class ItemMapperImpl implements ItemMapper{

    public ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() == null ? null : item.getRequest())
                .build();
    }

    public Item fromDTO(ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .build();
    }
}
