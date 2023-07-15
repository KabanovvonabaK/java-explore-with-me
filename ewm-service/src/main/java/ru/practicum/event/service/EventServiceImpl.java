package ru.practicum.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsClient;
import ru.practicum.StatsDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.enums.EventState;
import ru.practicum.enums.StateAction;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.user.service.UserService;
import ru.practicum.utils.adapters.DateTimeAdapter;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatsClient statsClient;

    public EventFullDto create(EventDto eventDto, int userId) {
        log.info("EventService.class create() userId{} with {}", userId, eventDto);
        checkEventDate(eventDto.getEventDate(), 2);
        categoryService.findById(eventDto.getCategory());

        Category category = CategoryMapper.toObject(categoryService.findById(eventDto.getCategory()));
        Event event = EventMapper.dtoToObject(eventDto, category);
        event.setCreatedOn(LocalDateTime.now());
        event.setStatus(EventState.PENDING);
        event.setViews(0);
        event.setInitiator(userService.findUserById(userId));
        event.setConfirmedRequests(0);

        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        return EventMapper.objectToFullDto(repository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(int userId, Integer from, Integer size) {
        log.info("EventService.class getUserEvents() userId {}, from {}, size {}", userId, from, size);
        PageRequest pageable = createPageable(from, size, null);
        return repository.findAllByInitiatorId(userId, pageable).stream().map(EventMapper::objectToShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventFullDto getUserEventById(int userId, int eventId) {
        log.info("EventService.class getUserEventById() userId {}, eventId {}", userId, eventId);
        userService.findUserById(userId);
        Event event = findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("User with id %d not initiator of event with id %d",
                    userId, eventId));
        } else {
            return EventMapper.objectToFullDto(event);
        }
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsForAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                                String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("EventService.class getEventsForAdmin() users {}, states {}, categories {}, rangeStart {}, " +
                "rangeEnd {}, from {}, size {}", users, states, categories, rangeStart, rangeEnd, from, size);
        PageRequest pageable = createPageable(from, size, null);
        Page<Event> eventPage = createRequestAdmin(users, states, categories, rangeStart, rangeEnd, pageable);
        Set<Event> eventsSet = eventPage.stream().collect(Collectors.toSet());
        return eventsSet.stream().map(EventMapper::objectToFullDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                         Integer size, String ip, String path) {
        log.info("EventService.class getEvents() text {}, categories {}, paid {}, rangeStart {}, " +
                        "rangeEnd {}, onlyAvailable {}, sort {}, from {}, size {}", text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);
        PageRequest pageable = createPageable(from, size, sort);
        Page<Event> eventPage = createRequestPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);
        Set<Event> eventsSet = eventPage.stream().collect(Collectors.toSet());
        createHit(ip, path);
        return eventsSet.stream().map(EventMapper::objectToShortDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventFullDto findById(int eventId, String ip, String path) {
        log.info("EventService.class findById() eventId {}", eventId);
        Event event = findEventById(eventId);
        if (!event.getStatus().equals(EventState.PUBLISHED)) {
            throw new EntityNotFoundException("Event must be published");
        }
        ResponseEntity<Object> response = statsClient.getStats(event.getCreatedOn(), LocalDateTime.now(),
                List.of(path), true);
        ObjectMapper mapper = new ObjectMapper();
        List<StatsDto> statsDto = mapper.convertValue(response.getBody(), new TypeReference<List<StatsDto>>() {
        });
        if (!statsDto.isEmpty()) {
            event.setViews(Math.toIntExact(statsDto.get(0).getHits()));
            repository.save(event);
        }
        createHit(ip, path);
        return EventMapper.objectToFullDto(event);
    }

    public EventFullDto updateEventByUser(UpdateEventDto updateEventDto, int userId, int eventId) {
        log.info("EventService.class updateEventByUser() event {}, userId = {}, eventId = {}",
                updateEventDto, userId, eventId);
        userService.findUserById(userId);
        int maximumHoursDeviation = 2;
        Event oldEvent = findEventById(eventId);
        if (!oldEvent.getInitiator().getId().equals(userId)) {
            throw new EntityNotFoundException(String.format("User with id %d not the initiator of event with id %d",
                    userId, eventId));
        } else if (oldEvent.getStatus().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Impossible to update published event");
        }
        Event newEvent = validateEvent(updateEventDto, oldEvent, maximumHoursDeviation, false);
        return EventMapper.objectToFullDto(repository.save(newEvent));
    }

    public EventFullDto updateEventByAdmin(UpdateEventDto updateEventDto, int eventId) {
        log.info("EventService.class updateEventByAdmin() event {}, eventId = {}", updateEventDto, eventId);
        Event event = findEventById(eventId);
        int maximumHoursDeviation = 1;
        Event newEvent = validateEvent(updateEventDto, event, maximumHoursDeviation, true);
        return EventMapper.objectToFullDto(repository.save(newEvent));
    }

    public void updateEventRequest(Event event) {
        repository.save(event);
    }

    public void updateEventRequest(List<Event> eventList) {
        repository.saveAll(eventList);
    }

    @Transactional(readOnly = true)
    public Event findEventById(int eventId) {
        return repository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException(String.format("No event with id %d", eventId)));
    }

    @Transactional(readOnly = true)
    public void findEventsByIds(List<Integer> events) {
        List<Event> foundedEvents = repository.findAllById(events);
        if (events.size() != foundedEvents.size()) {
            throw new EntityNotFoundException("Not all events found from the list.");
        }
    }

    private void createHit(String ip, String path) {
        statsClient.createHit(EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(path)
                .ip(ip)
                .timestamp(DateTimeAdapter.dateToString(LocalDateTime.now()))
                .build());
    }

    private void checkEventDate(String eventDate, int hours) {
        if (Objects.requireNonNull(DateTimeAdapter.stringToDate(eventDate))
                .isBefore(LocalDateTime.now().plusHours(hours))) {
            throw new DateTimeException("Event date can't be in the past.");
        }
    }

    private PageRequest createPageable(Integer from, Integer size, String sort) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("the from parameter must be greater than or equal to 0; size is greater than 0");
        }
        if (sort == null || sort.isEmpty()) {
            return PageRequest.of(from / size, size);
        }
        switch (sort) {
            case "EVENT_DATE":
                return PageRequest.of(from / size, size, Sort.by("eventDate"));
            case "VIEWS":
                return PageRequest.of(from / size, size, Sort.by("views").descending());
            default:
                throw new BadRequestException("Unknown sort: " + sort);
        }
    }

    private Event validateEvent(UpdateEventDto updateEventDto, Event event, int hours, boolean checkAdmin) {
        if (updateEventDto.getEventDate() != null) {
            checkEventDate(updateEventDto.getEventDate(), hours);
            event.setEventDate(DateTimeAdapter.stringToDate(updateEventDto.getEventDate()));
        }
        if (updateEventDto.getAnnotation() != null && !updateEventDto.getAnnotation().isBlank()) {
            event.setAnnotation(updateEventDto.getAnnotation());
        }
        if (updateEventDto.getCategory() != null) {
            event.setCategory(CategoryMapper.toObject(categoryService.findById(updateEventDto.getCategory())));
        }
        if (updateEventDto.getDescription() != null && !updateEventDto.getDescription().isBlank()) {
            if (updateEventDto.getDescription().length() < 20 || updateEventDto.getDescription().length() > 7000) {
                throw new BadRequestException("Description length should be from 20 up to 7000 length.");
            } else {
                event.setDescription(updateEventDto.getDescription());
            }
        }
        if (updateEventDto.getLocation() != null) {
            event.setLat(updateEventDto.getLocation().getLat());
            event.setLon(updateEventDto.getLocation().getLon());
        }
        if (updateEventDto.getPaid() != null) {
            event.setPaid(updateEventDto.getPaid());
        }
        if (updateEventDto.getParticipantLimit() != null) {
            if (updateEventDto.getParticipantLimit() < 1) {
                throw new BadRequestException("ParticipantLimit must be positive");
            }
            event.setParticipantLimit(updateEventDto.getParticipantLimit());
        }
        if (updateEventDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventDto.getRequestModeration());
        }
        if (updateEventDto.getStateAction() != null) {
            if (!checkAdmin) {
                if (event.getStatus().equals(EventState.CANCELED) &&
                        updateEventDto.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                    event.setStatus(EventState.PENDING);
                } else if (!event.getStatus().equals(EventState.CANCELED) &&
                        updateEventDto.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                    event.setStatus(EventState.CANCELED);
                }
            } else {
                if (event.getStatus().equals(EventState.PENDING) &&
                        updateEventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                    event.setStatus(EventState.PUBLISHED);
                } else if (!event.getStatus().equals(EventState.PENDING) &&
                        updateEventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                    throw new ConflictException("Wrong state");
                }
                if (!event.getStatus().equals(EventState.PUBLISHED)
                        && updateEventDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                    event.setStatus(EventState.CANCELED);
                    event.setPublishedOn(LocalDateTime.now());
                } else if (event.getStatus().equals(EventState.PUBLISHED) &&
                        updateEventDto.getStateAction().equals(StateAction.REJECT_EVENT)) {
                    throw new ConflictException("Wrong state");
                }
            }
        }
        if (updateEventDto.getTitle() != null && !updateEventDto.getTitle().isBlank()) {
            if (updateEventDto.getTitle().length() < 3 || updateEventDto.getTitle().length() > 120) {
                throw new BadRequestException("Title length should be from 3 up to 120 length.");
            } else {
                event.setTitle(updateEventDto.getTitle());
            }
        }
        return event;
    }

    private Page<Event> createRequestPublic(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable, PageRequest pageable) {
        return repository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Predicate textOr1 = criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),
                    "%" + text.toUpperCase() + "%");
            Predicate textOr2 = criteriaBuilder.like(criteriaBuilder.upper(root.get("annotation")),
                    "%" + text.toUpperCase() + "%");
            Predicate predicateForGrade = criteriaBuilder.or(textOr1, textOr2);
            predicates.add(predicateForGrade);
            if (categories != null && !categories.isEmpty()) {
                CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("category"));
                for (Integer categoriesId : categories) {
                    in.value(categoriesId);
                }
                predicates.add(in);
            }

            if (paid != null) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("paid"), paid)));
            }

            LocalDateTime start = DateTimeAdapter.stringToDate(rangeStart);
            LocalDateTime end = DateTimeAdapter.stringToDate(rangeEnd);
            if (start != null && end != null && start.isAfter(end)) {
                throw new BadRequestException("the beginning of the range cannot start before the end of the range");
            }
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), Objects.requireNonNullElseGet(start, LocalDateTime::now)));
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }

            predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), EventState.PUBLISHED)));

            if (onlyAvailable) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"))));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    private Page<Event> createRequestAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                           String rangeStart, String rangeEnd, PageRequest pageable) {
        return repository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (users != null && !users.isEmpty() && !(users.size() == 1 && users.get(0) == 0)) {
                CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("initiator"));
                for (Integer initiatorId : users) {
                    in.value(initiatorId);
                }
                predicates.add(in);
            }

            if (states != null && !states.isEmpty()) {

                List<EventState> eventStates = states.stream().map(EventState::valueOf).collect(Collectors.toList());

                CriteriaBuilder.In<EventState> in = criteriaBuilder.in(root.get("status"));
                for (EventState status : eventStates) {
                    in.value(status);
                }
                predicates.add(in);
            }

            if (categories != null && !categories.isEmpty()) {
                CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("category"));
                for (Integer categoriesId : categories) {
                    in.value(categoriesId);
                }
                predicates.add(in);
            }

            LocalDateTime start = DateTimeAdapter.stringToDate(rangeStart);
            LocalDateTime end = DateTimeAdapter.stringToDate(rangeEnd);
            if (start != null && end != null && start.isAfter(end)) {
                throw new BadRequestException("the beginning of the range cannot start before the end of the range");
            }
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
                    Objects.requireNonNullElseGet(start, LocalDateTime::now)));
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
}