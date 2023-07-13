package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class EventController {
    private final EventService service;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@Valid @RequestBody EventDto eventDto,
                               @PathVariable int userId) {
        log.info("EventController.class create() user id {} with {}", userId, eventDto);
        return service.create(eventDto, userId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(value = "text", defaultValue = "") String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(value = "rangeStart", defaultValue = "no date")
                                             String rangeStart,
                                         @RequestParam(value = "rangeEnd", defaultValue = "no date") String rangeEnd,
                                         @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(value = "sort", defaultValue = "") String sort,
                                         @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        log.info("EventController.class getEvents()");
        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/events/{id}")
    public EventFullDto findById(@PathVariable int id, HttpServletRequest request) {
        log.info("EventController.class findById() id {}", id);
        return service.findById(id, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getUserEvents(@PathVariable int userId,
                                             @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("EventController.class getUserEvents() userId {}", userId);
        return service.getUserEvents(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUserEventById(@PathVariable int userId,
                                         @PathVariable int eventId) {
        log.info("EventController.class getUserEventById() userId {} eventId {}", userId, eventId);
        return service.getUserEventById(userId, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsForAdmin(@RequestParam(required = false) List<Integer> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Integer> categories,
                                                @RequestParam(value = "rangeStart", defaultValue = "no date")
                                                    String rangeStart,
                                                @RequestParam(value = "rangeEnd", defaultValue = "no date")
                                                    String rangeEnd,
                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("EventController.class getEventsForAdmin()");
        return service.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@RequestBody UpdateEventDto updateEventDto,
                                          @PathVariable int userId,
                                          @PathVariable int eventId) {
        log.info("EventController.class updateEventByUser() userId {} eventId {} with {}",
                userId, eventId, updateEventDto);
        return service.updateEventByUser(updateEventDto, userId, eventId);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@RequestBody UpdateEventDto updateEventDto,
                                           @PathVariable int eventId) {
        log.info("EventController.class updateEventByAdmin() eventId{} with {}", eventId, updateEventDto);
        return service.updateEventByAdmin(updateEventDto, eventId);
    }
}