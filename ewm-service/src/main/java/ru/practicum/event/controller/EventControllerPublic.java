package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventControllerPublic {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(value = "text", defaultValue = "") String text,
                                      @RequestParam(required = false) List<Integer> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(value = "rangeStart", defaultValue = "no date")
                                                   String rangeStart,
                                      @RequestParam(value = "rangeEnd", defaultValue = "no date")
                                                   String rangeEnd,
                                      @RequestParam(defaultValue = "false")
                                                   Boolean onlyAvailable,
                                      @RequestParam(value = "sort", defaultValue = "") String sort,
                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                      @Positive @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      HttpServletRequest request) {
        log.info("EventControllerPublic.class getAll()");
        return service.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable int id, HttpServletRequest request) {
        log.info("EventController.class findById() id {}", id);
        return service.findById(id, request.getRemoteAddr(), request.getRequestURI());
    }
}