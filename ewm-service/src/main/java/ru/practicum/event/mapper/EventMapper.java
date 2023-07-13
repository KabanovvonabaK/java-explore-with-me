package ru.practicum.event.mapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.utils.adapters.DateTimeAdapter;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Location;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.category.service.CategoryService;

@UtilityClass
public class EventMapper {
    public EventShortDto objectToShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .eventDate(DateTimeAdapter.dateToString(event.getEventDate()))
                .category(CategoryMapper.toDto(event.getCategory()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserMapper.objectToShortDto(event.getInitiator()))
                .views(event.getViews())
                .build();
    }

    public EventFullDto objectToFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(DateTimeAdapter.dateToString(event.getEventDate()))
                .category(CategoryMapper.toDto(event.getCategory()))
                .location(new Location(event.getLon(), event.getLat()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(DateTimeAdapter.dateToString(event.getCreatedOn()))
                .initiator(UserMapper.objectToShortDto(event.getInitiator()))
                .publishedOn(event.getPublishedOn() != null ? DateTimeAdapter.dateToString(event.getPublishedOn()) : null)
                .state(event.getStatus())
                .views(event.getViews())
                .build();
    }

    public Event dtoToObject(EventDto dto, Category category) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(DateTimeAdapter.stringToDate(dto.getEventDate()))
                .category(category)
                .lon(dto.getLocation().getLon())
                .lat(dto.getLocation().getLat())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .build();
    }
}