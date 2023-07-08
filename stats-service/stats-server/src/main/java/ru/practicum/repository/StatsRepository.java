package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(distinct h.ip) as hits) " +
            "from EndpointHit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<Stats> findAllUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(h.ip) as hits) " +
            "from EndpointHit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(distinct h.ip) as hits) " +
            "from EndpointHit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<Stats> findAllUniqueStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.model.Stats(h.app, h.uri, count(h.ip) as hits) " +
            "from EndpointHit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "and h.uri in (?3) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> findAllStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}