package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationControllerAdmin {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Validated(Create.class) @RequestBody NewCompilationDto newCompilationDto) {
        log.info("CompilationControllerAdmin.class create() {}", newCompilationDto);
        return service.create(newCompilationDto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@Validated(Update.class) @RequestBody NewCompilationDto newCompilationDto,
                                 @PathVariable int compId) {
        log.info("CompilationControllerAdmin.class update() id {} with {}", compId, newCompilationDto);
        return service.update(newCompilationDto, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int compId) {
        log.info("CompilationControllerAdmin.class delete() id {}", compId);
        service.delete(compId);
    }
}