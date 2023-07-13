package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.enums.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@Builder
public class EventFullDto {
    private final String annotation;
    private final CategoryDto category;
    private final Integer confirmedRequests;
    private final String createdOn;
    private final String description;
    private final String eventDate;
    private final Integer id;
    private final UserShortDto initiator;
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final String publishedOn;
    private final Boolean requestModeration;
    private final EventState state;
    private final String title;
    private final Integer views;
}