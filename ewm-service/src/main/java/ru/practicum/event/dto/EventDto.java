package ru.practicum.event.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.event.model.Location;
import ru.practicum.utils.validation.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class EventDto {
    @NotBlank(groups = Create.class, message = "Event annotation should not be empty or blanked.")
    @Size(min = 20, max = 2000, groups = Create.class, message = "Event annotation should be from 20 up to 2000 length.")
    private final String annotation;
    @NotNull
    private final Integer category;
    @NotBlank(groups = Create.class, message = "Event description should not be empty or blanked.")
    @Size(min = 20, max = 7000, groups = Create.class, message = "Event description should be from 20 up to 7000 length.")
    private final String description;
    @NotNull
    private String eventDate;
    @NotNull
    private final Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(groups = Create.class, message = "Event title should not be empty or blanked.")
    @Size(min = 3, max = 120, groups = Create.class, message = "Event title should be from 3 up to 120 length.")
    private final String title;
}