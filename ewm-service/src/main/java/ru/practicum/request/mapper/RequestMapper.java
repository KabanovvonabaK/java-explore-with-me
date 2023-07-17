package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.enums.RequestState;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;
import ru.practicum.utils.adapters.DateTimeAdapter;

@UtilityClass
public class RequestMapper {
    public Request dtoToObject(RequestDto dto, Event event, User requester) {
        return Request.builder()
                .id(dto.getId())
                .status(RequestState.valueOf(dto.getStatus()))
                .created(DateTimeAdapter.stringToDate(dto.getCreated()))
                .event(event)
                .requester(requester)
                .build();
    }

    public RequestDto objectToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .status(request.getStatus().toString())
                .created(DateTimeAdapter.dateToString(request.getCreated()))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .build();
    }
}