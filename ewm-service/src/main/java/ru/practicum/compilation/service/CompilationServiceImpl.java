package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.service.EventService;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventService eventService;

    public CompilationDto create(NewCompilationDto newCompilationDto) {
        log.info("CompilationService.class create() {}", newCompilationDto);
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        eventVerification(newCompilationDto.getEvents());
        return CompilationMapper.toDto(repository.save(CompilationMapper.toObject(newCompilationDto, eventService)));
    }

    public CompilationDto update(NewCompilationDto newCompilationDto, int id) {
        log.info("CompilationService.class update() id {} with {}", id, newCompilationDto);
        Compilation compilation = getCompilationById(id);
        if (newCompilationDto.getTitle() != null && !newCompilationDto.getTitle().isEmpty()
                && !newCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            eventVerification(newCompilationDto.getEvents());
            compilation.setEvents(newCompilationDto.getEvents()
                    .stream()
                    .map(eventService::findEventById)
                    .collect(Collectors.toList()));
        }
        return CompilationMapper.toDto(repository.save(compilation));
    }

    public void delete(int id) {
        log.info("CompilationService.class delete() id {}", id);
        findById(id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        log.info("CompilationService.class getAll()");
        List<Compilation> compilations = repository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        return compilations.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompilationDto findById(int id) {
        log.info("CompilationService.class findById() id {}", id);
        return CompilationMapper.toDto(repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("No compilation with id %d", id))));
    }

    private Compilation getCompilationById(int id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("No compilation with id %d", id)));
    }

    private void eventVerification(List<Integer> events) {
        if (events != null) {
            eventService.findEventsByIds(events);
        }
    }
}