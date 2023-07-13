package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestState;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.dto.UpdateRequestDto;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestUpdateResult;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;
import ru.practicum.utils.adapters.DateTimeAdapter;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final EventService eventService;
    private final UserService userService;

    public RequestDto create(int userId, int eventId) {
        log.info("RequestService.class create() userId {}, eventId {}", userId, eventId);
        userService.findUserById(userId);
        eventService.findEventById(eventId);
        RequestDto dto = validateNewRequest(userId, eventId);
        Event event = eventService.findEventById(dto.getEvent());
        User requester = userService.findUserById(dto.getRequester());
        Request request = repository.save(RequestMapper.dtoToObject(dto, event, requester));
        updateEventRequest(request, 1);
        return RequestMapper.objectToDto(request);
    }

    public List<RequestDto> getRequestDto(int userId) {
        log.info("RequestService.class getRequestDto() userId {}", userId);
        userService.findUserById(userId);
        return repository.findByRequesterId(userId)
                .stream()
                .map(RequestMapper::objectToDto)
                .collect(Collectors.toList());
    }

    public List<RequestDto> getEventRequestPrivate(int userId, int eventId) {
        log.info("RequestService.class getEventRequestPrivate() userId {}, eventId {}", userId, eventId);
        userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("User with id %d not author of event with id %d",
                    userId, eventId));
        }
        return repository.findByEventInitiatorIdAndEventId(userId, eventId).stream().map(RequestMapper::objectToDto)
                .sorted(Comparator.comparing(RequestDto::getStatus)).collect(Collectors.toList());
    }

    public RequestDto updateRequest(int userId, int requestId) {
        log.info("RequestService.class updateRequest() userId = {}, requestId = {}", userId, requestId);
        userService.findUserById(userId);
        Request request = findRequestById(requestId);
        updateEventRequest(request, -1);
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.objectToDto(repository.save(request));
    }

    public RequestUpdateResult updateRequestPrivate(UpdateRequestDto updateRequestDto, int userId, int eventId) {
        log.info("RequestService.class updateRequestPrivate() userId = {}, eventId = {} with {}", userId, eventId, updateRequestDto);
        userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException(String.format("User with id %d not author of event with id %d",
                    userId, eventId));
        }
        requestProcessing(event, updateRequestDto);
        List<Integer> ids = updateRequestDto.getRequestIds();
        List<RequestDto> confirmedRequests = repository
                .findAllByIdInAndEventInitiatorIdAndEventIdAndStatus(ids, userId, eventId, RequestState.CONFIRMED)
                .stream()
                .map(RequestMapper::objectToDto)
                .collect(Collectors.toList());
        List<RequestDto> rejectedRequests = repository
                .findAllByIdInAndEventInitiatorIdAndEventIdAndStatus(ids, userId, eventId, RequestState.REJECTED)
                .stream()
                .map(RequestMapper::objectToDto)
                .collect(Collectors.toList());
        return RequestUpdateResult.builder().confirmedRequests(confirmedRequests).rejectedRequests(rejectedRequests).build();
    }

    private void requestProcessing(Event event, UpdateRequestDto dto) {
        for (Integer requestId : dto.getRequestIds()) {
            Request request = findRequestById(requestId);
            if (!request.getStatus().equals(RequestState.PENDING)) {
                throw new ConflictException("Status of request is PENDING");
            }
            switch (dto.getStatus()) {
                case CONFIRMED:
                    cancelRequests(event);
                    request.setStatus(RequestState.CONFIRMED);
                    repository.save(request);
                    updateEventRequest(request, 1);
                    break;
                case REJECTED:
                    request.setStatus(RequestState.REJECTED);
                    repository.save(request);
                    updateEventRequest(request, -1);
                    break;
                default:
                    throw new BadRequestException("Unexpected case in RequestService.class requestProcessing()");
            }
        }
    }

    private void cancelRequests(Event event) {
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            List<Request> canceledRequests = repository.findAllByEventIdAndStatus(event.getId(), RequestState.PENDING)
                    .stream()
                    .peek(request -> request.setStatus(RequestState.CANCELED))
                    .collect(Collectors.toList());
            repository.saveAll(canceledRequests);
            throw new ConflictException("Limit of applications for this event has already been exhausted.");
        }
    }

    private Request findRequestById(int requestId) {
        return repository.findById(requestId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Request with id %d not exist.", requestId)));
    }

    private void updateEventRequest(Request request, int change) {
        if (request.getStatus().equals(RequestState.CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() + change);
            eventService.updateEventRequest(event);
        }
    }

    private RequestDto validateNewRequest(int requesterId, int eventId) {
        RequestDto dto = RequestDto.builder()
                .requester(requesterId)
                .event(eventId)
                .created(DateTimeAdapter.dateToString(LocalDateTime.now()))
                .build();
        Optional<Request> checkRequest = repository.findAllByEventIdAndRequesterId(dto.getEvent(), requesterId);
        Event event = eventService.findEventById(dto.getEvent());
        if (checkRequest.isPresent()) {
            throw new ConflictException("Request already exist.");
        }
        if (event.getInitiator().getId().equals(requesterId)) {
            throw new ConflictException("Initiator can't be requester.");
        }
        if (!event.getStatus().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event is not published yet.");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Limit of participation requests for event.");
        }
        if (event.getParticipantLimit() == 0) {
            dto.setStatus("CONFIRMED");
        } else if (event.getRequestModeration()) {
            dto.setStatus("PENDING");
        } else {
            dto.setStatus("CONFIRMED");
        }
        return dto;
    }
}