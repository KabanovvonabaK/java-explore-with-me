package ru.practicum.event.service;

import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventService {
    EventFullDto create(EventDto eventDto, int userId);

    List<EventShortDto> getUserEvents(int userId, Integer from, Integer size);

    EventFullDto getUserEventById(int userId, int eventId);

    List<EventFullDto> getEventsForAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                         String rangeStart, String rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                  String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                  Integer size, String ip, String path);

    EventFullDto findById(int eventId, String ip, String path);

    EventFullDto updateEventByUser(UpdateEventDto updateEventDto, int userId, int eventId);

    EventFullDto updateEventByAdmin(UpdateEventDto updateEventDto, int eventId);

    void updateEventRequest(Event event);

    void updateEventRequest(List<Event> eventList);

    Event findEventById(int eventId);
    
    void findEventsByIds(List<Integer> events);
}