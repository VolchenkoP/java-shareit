package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.config.constants.HttpHeaders;
import ru.practicum.shareit.request.dto.ItemRequestDtoToAdd;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String HEADER = HttpHeaders.USER_HEADER;
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(HEADER) Long userId,
                                                 @Valid @RequestBody ItemRequestDtoToAdd itemRequestAddDto) {
        log.info("Add item request: {}, userId: {}", itemRequestAddDto, userId);
        return requestClient.addItemRequest(userId, itemRequestAddDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequests(@RequestHeader(HEADER) Long userId) {
        log.info("Get item requests userId: {}", userId);
        return requestClient.getItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(HEADER) Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "20") int size) {
        log.info("Get item requests all users from: {}, size: {}", from, size);
        return requestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(HEADER) Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Get item request: {}", requestId);
        return requestClient.getItemRequest(userId, requestId);
    }
}
