package ru.practicum.shareit.item.mapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dto.ExtendedItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemFromUpdateRequestDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class ItemMapper {

    private static ExtendedItemDto.LastNextBooking getLast(List<Booking> bookingList) {
        return bookingList.stream()
                .filter(b -> b.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .map(lb -> new ExtendedItemDto.LastNextBooking(lb.getId(), lb.getBooker().getId()))
                .orElse(null);
    }

    private static ExtendedItemDto.LastNextBooking getNext(List<Booking> bookingList) {
        return bookingList.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .map(nb -> new ExtendedItemDto.LastNextBooking(nb.getId(), nb.getBooker().getId()))
                .orElse(null);
    }

    public ItemDTO toDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() == null ? null : item.getRequest())
                .build();
    }

    public Item fromDTO(ItemCreateDto itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .build();
    }

    public Item fromUpdateDto(ItemFromUpdateRequestDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public ExtendedItemDto toExtendedItemDto(Item item, List<Booking> bookingList) {
        ExtendedItemDto extendedItemDto = ExtendedItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
        List<Comment> comments = new ArrayList<>(item.getComments());
        log.debug("у объекта бронирования следующие комментарии: {}", comments);
        if (!CollectionUtils.isEmpty(comments)) {
            extendedItemDto.setComments(comments.stream()
                    .map(CommentMapper::toCommentResponseDto).toList());
        }
        if (!CollectionUtils.isEmpty(bookingList)) {
            extendedItemDto.setLastBooking(getLast(bookingList));
            extendedItemDto.setNextBooking(getNext(bookingList));
        }
        return extendedItemDto;
    }

    public List<ExtendedItemDto> toListExtendedItemDto(List<Item> items, List<Booking> bookingList) {
        List<ExtendedItemDto> extendedItemDtos = new ArrayList<>();
        for (Item item : items) {
            ExtendedItemDto extendedItemDto = ExtendedItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .request(item.getRequest())
                    .build();
            List<Comment> comments = new ArrayList<>(item.getComments());
            log.debug("у объекта броинрования c id: {} следующие комментарии: {}", item.getId(), comments);
            if (!CollectionUtils.isEmpty(comments)) {
                extendedItemDto.setComments(comments.stream()
                        .map(CommentMapper::toCommentResponseDto).toList());
            }
            List<Booking> bookings = bookingList.stream()
                    .filter(booking -> booking.getItem().getId().equals(item.getId()))
                    .toList();
            extendedItemDto.setLastBooking(getLast(bookings));
            extendedItemDto.setNextBooking(getNext(bookings));
            extendedItemDtos.add(extendedItemDto);
        }
        return extendedItemDtos;
    }

    public static ItemOwnerDto toItemOwnerDto(Item item) {
        return ItemOwnerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }
}
