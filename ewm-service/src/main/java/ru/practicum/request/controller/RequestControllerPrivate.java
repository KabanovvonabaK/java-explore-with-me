package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.UpdateRequestDto;
import ru.practicum.request.model.RequestUpdateResult;
import ru.practicum.request.service.RequestService;
import ru.practicum.utils.validation.Update;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
public class RequestControllerPrivate {
    private final RequestService service;

    @GetMapping
    public List<RequestDto> getEventRequestPrivate(@PathVariable int userId, @PathVariable int eventId) {
        log.info("RequestController.class getEventRequestPrivate() userId {} eventId {}", userId, eventId);
        return service.getEventRequestPrivate(userId, eventId);
    }

    @PatchMapping
    public RequestUpdateResult updateRequestPrivate(@Validated(Update.class) @RequestBody UpdateRequestDto updateRequestDto,
                                                    @PathVariable int userId,
                                                    @PathVariable int eventId) {
        log.info("RequestController.class updateRequestPrivate() userId {} eventId {} with {}",
                userId, eventId, updateRequestDto);
        return service.updateRequestPrivate(updateRequestDto, userId, eventId);
    }
}