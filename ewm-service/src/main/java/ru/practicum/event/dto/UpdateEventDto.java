package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.StateAction;
import ru.practicum.event.model.Location;

@Data
@AllArgsConstructor
@Builder
public class UpdateEventDto {
    private final String annotation;
    private final Integer category;
    private final String description;
    private String eventDate;
    private final Location location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
    private StateAction stateAction;
    private final String title;
}