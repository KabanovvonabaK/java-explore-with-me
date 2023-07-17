package ru.practicum.request.service;

import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.UpdateRequestDto;
import ru.practicum.request.model.RequestUpdateResult;

import java.util.List;

public interface RequestService {
    RequestDto create(int userId, int eventId);

    List<RequestDto> getRequestDto(int userId);

    List<RequestDto> getEventRequestPrivate(int userId, int eventId);

    RequestDto updateRequest(int userId, int requestId);

    RequestUpdateResult updateRequestPrivate(UpdateRequestDto updateRequestDto, int userId, int eventId);
}