package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.RequestUpdateResult;
import ru.practicum.request.dto.UpdateRequestDto;
import ru.practicum.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class RequestController {

    private final RequestService service;

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDto> getEventRequestPrivate(@PathVariable int userId, @PathVariable int eventId) {
        log.info("RequestController.class getEventRequestPrivate() userId {} eventId {}", userId, eventId);
        return service.getEventRequestPrivate(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public RequestUpdateResult updateRequestPrivate(@Valid @RequestBody UpdateRequestDto updateRequestDto,
                                                    @PathVariable int userId,
                                                    @PathVariable int eventId) {
        log.info("RequestController.class updateRequestPrivate() userId {} eventId {} with {}",
                userId, eventId, updateRequestDto);
        return service.updateRequestPrivate(updateRequestDto, userId, eventId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@RequestParam int eventId,
                             @PathVariable int userId) {
        log.info("RequestController.class createRequest() userId {} eventId {}", userId, eventId);
        return service.create(userId, eventId);
    }

    @GetMapping("/requests")
    public List<RequestDto> getRequest(@PathVariable int userId) {
        log.info("RequestController.class getRequest() userId {}", userId);
        return service.getRequestDto(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDto updateRequest(@PathVariable int userId,
                                    @PathVariable int requestId) {
        log.info("RequestController.class updateRequest() userId {} requestId {}", userId, requestId);
        return service.updateRequest(userId, requestId);
    }
}