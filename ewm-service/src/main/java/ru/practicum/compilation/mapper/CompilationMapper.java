package ru.practicum.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream().map(EventMapper::objectToShortDto).collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .build();
    }

    public Compilation toObject(NewCompilationDto dto, EventService eventService) {
        List<Event> events = new ArrayList<>();

        if (dto.getEvents() != null) {
            events = dto.getEvents().stream().map(eventService::findEventById).collect(Collectors.toList());
        }

        return Compilation.builder()
                .title(dto.getTitle())
                .events(events)
                .pinned(dto.getPinned())
                .build();
    }
}