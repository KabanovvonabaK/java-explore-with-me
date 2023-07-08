package ru.practicum.model.mapper;

import ru.practicum.StatsDto;
import ru.practicum.model.Stats;

public class StatsMapper {
    public static StatsDto toStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }
}