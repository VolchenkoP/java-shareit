package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Named;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOwnerDto {
    private Long id;
    private String name;
    private Long ownerId;
}
