package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto update(NewCompilationDto newCompilationDto, int id);

    void delete(int id);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto findById(int id);
}