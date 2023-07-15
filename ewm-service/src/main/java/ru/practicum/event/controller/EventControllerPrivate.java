package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventControllerPrivate {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Validated(Create.class) @RequestBody EventDto eventDto,
                               @PathVariable int userId) {
        log.info("EventControllerPrivate.class create() user id {} with {}", userId, eventDto);
        return eventService.create(eventDto, userId);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable int userId,
                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("EventControllerPrivate.class getUserEvents() userId {}", userId);
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEventById(@PathVariable int userId,
                                         @PathVariable int eventId) {
        log.info("EventControllerPrivate.class getUserEventById() userId {} eventId {}", userId, eventId);
        return eventService.getUserEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@Validated(Update.class) @RequestBody UpdateEventDto updateEventDto,
                                          @PathVariable int userId,
                                          @PathVariable int eventId) {
        log.info("EventControllerPrivate.class updateEventByUser() eventId{} with {}", eventId, updateEventDto);
        return eventService.updateEventByUser(updateEventDto, userId, eventId);
    }
}