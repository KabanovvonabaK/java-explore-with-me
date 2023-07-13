package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.enums.EventState;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private final Integer id;
    @Column(name = "ANNOTATION", nullable = false)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User initiator;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;
    @Column(name = "LON")
    private Float lon;
    @Column(name = "LAT")
    private Float lat;
    @Column(name = "PAID")
    private Boolean paid;
    @Column(name = "PARTICIPANT_LIMIT")
    private Integer participantLimit;
    @Column(name = "REQUEST_MODERATION")
    private Boolean requestModeration;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private EventState status;
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    @Column(name = "PUBLISHED_ON")
    private LocalDateTime publishedOn;
    @Column(name = "VIEWS")
    private Integer views;
    @Column(name = "CONFIRMED_REQUESTS")
    private Integer confirmedRequests;
}