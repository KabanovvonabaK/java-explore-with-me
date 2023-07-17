package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "HITS")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private final int id;
    @Column(name = "APP", nullable = false)
    private final String app;
    @Column(name = "URI", nullable = false)
    private final String uri;
    @Column(name = "IP", nullable = false)
    private final String ip;
    @Column(name = "TIMESTAMP")
    private final LocalDateTime timestamp;
}