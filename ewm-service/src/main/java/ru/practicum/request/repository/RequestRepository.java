package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.enums.RequestState;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    Optional<Request> findAllByEventIdAndRequesterId(Integer event, int userId);

    List<Request> findByRequesterId(int userId);

    List<Request> findAllByEventIdAndStatus(int eventId, RequestState requestState);

    List<Request> findByEventInitiatorIdAndEventId(int userId, int eventId);

    List<Request> findAllByIdInAndEventInitiatorIdAndEventIdAndStatus(List<Integer> ids, int userId, int eventId,
                                                                      RequestState confirmed);
}