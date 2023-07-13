package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@Builder
public class EventShortDto {
    private final String annotation;
    private final CategoryDto category;
    private final Integer confirmedRequests;
    private final String eventDate;
    private final Integer id;
    private final UserShortDto initiator;
    private final Boolean paid;
    private final String title;
    private final Integer views;
}