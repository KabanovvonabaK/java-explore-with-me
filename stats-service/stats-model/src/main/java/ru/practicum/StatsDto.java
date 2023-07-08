package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StatsDto {
    private final String app;
    private final String uri;
    private final Long hits;
}