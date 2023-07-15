package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Integer> users,
                                     @RequestParam(required = false) List<String> states,
                                     @RequestParam(required = false) List<Integer> categories,
                                     @RequestParam(value = "rangeStart", defaultValue = "no date") String rangeStart,
                                     @RequestParam(value = "rangeEnd", defaultValue = "no date") String rangeEnd,
                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("EventControllerAdmin.class getAll()");
        return service.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@Validated(Update.class) @RequestBody UpdateEventDto updateEventDto,
                               @PathVariable int eventId) {
        log.info("EventControllerAdmin.class update() eventId{} with {}", eventId, updateEventDto);
        return service.updateEventByAdmin(updateEventDto, eventId);
    }
}