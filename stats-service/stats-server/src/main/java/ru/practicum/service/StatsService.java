package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.mapper.EndpointHitMapper;
import ru.practicum.repository.StatsRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository statsRepository;

    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        log.info("Creating hit {}", endpointHitDto);
        return EndpointHitMapper.toEndpointHitDto(endpointHit);
    }

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Get stats with start {}, end {}, uris {} and unique {}", start, end, uris, unique);
        if (end.isBefore(start)) {
            throw new DateTimeException("End should not be before start.");
        }
        if ((uris == null || uris.isEmpty())) {
            if (unique) {
                return statsRepository.findAllUniqueStats(start, end);
            } else {
                return statsRepository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.findAllUniqueStatsByUris(start, end, uris);
            } else {
                return statsRepository.findAllStatsByUris(start, end, uris);
            }
        }
    }
}