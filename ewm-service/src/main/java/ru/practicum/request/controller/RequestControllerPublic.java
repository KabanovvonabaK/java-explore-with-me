package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestControllerPublic {
    private final RequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@RequestParam int eventId,
                             @PathVariable int userId) {
        log.info("RequestController.class createRequest() userId {} eventId {}", userId, eventId);
        return service.create(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequest(@PathVariable int userId) {
        log.info("RequestController.class getRequest() userId {}", userId);
        return service.getRequestDto(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto updateRequest(@PathVariable int userId,
                                    @PathVariable int requestId) {
        log.info("RequestController.class updateRequest() userId {} requestId {}", userId, requestId);
        return service.updateRequest(userId, requestId);
    }
}