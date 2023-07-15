package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.StateAction;
import ru.practicum.event.model.Location;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UpdateEventDto {
    @Size(min = 20, max = 2000, groups = Update.class,
            message = "Event annotation should be from 20 up to 2000 length.")
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