package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "description", source = "description")
    ItemRequest toItemRequest(ItemRequestDtoToAdd requestDtoToAdd);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "requester", source = "requester")
    @Mapping(target = "created", source = "created")
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "items", expression = "java(mapItems(itemRequest.getItems()))")
    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest);

    default List<ItemOwnerDto> mapItems(Set<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemOwnerDto)
                .toList();
    }

    List<ItemRequestResponseDto> toItemRequestResponseDtos(List<ItemRequest> itemRequests);

}
