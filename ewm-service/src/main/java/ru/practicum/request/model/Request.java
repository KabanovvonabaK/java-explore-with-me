package ru.practicum.request.model;

import lombok.*;
import ru.practicum.enums.RequestState;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "REQUESTS")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private final Integer id;
    @Column(name = "REQUEST_DATE")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "EVENT_ID")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private RequestState status;
}