package ru.practicum.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "COMPILATIONS")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPILATION_ID")
    private final Integer id;
    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Event.class)
    @JoinTable(name = "COMPILATIONS_EVENTS",
            joinColumns = @JoinColumn(name = "COMPILATION_ID", referencedColumnName = "COMPILATION_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID", referencedColumnName = "EVENT_ID"))
    private List<Event> events;
    @Column(name = "PINNED")
    private Boolean pinned;
    @Column(name = "TITLE", nullable = false)
    private String title;
}