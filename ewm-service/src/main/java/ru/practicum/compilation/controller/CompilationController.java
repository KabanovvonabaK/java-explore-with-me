package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("CompilationController.class create() {}", newCompilationDto);
        return service.create(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@RequestBody NewCompilationDto newCompilationDto,
                                 @PathVariable int compId) {
        log.info("CompilationController.class update() id {} with {}", compId, newCompilationDto);
        return service.update(newCompilationDto, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int compId) {
        log.info("CompilationController.class delete() id {}", compId);
        service.delete(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("CompilationController.class getAll()");
        return service.getAll(pinned, from, size);
    }

    @GetMapping("compilations/{compId}")
    public CompilationDto findById(@PathVariable int compId) {
        log.info("CompilationController.class findById() id {}", compId);
        return service.findById(compId);
    }
}