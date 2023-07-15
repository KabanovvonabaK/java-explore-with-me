package ru.practicum.user.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private final Integer id;
    @Column(name = "NAME", nullable = false, length = 320)
    private final String name;
    @Column(name = "EMAIL", nullable = false, length = 320, unique = true)
    private final String email;
}