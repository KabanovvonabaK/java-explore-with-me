package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationControllerPublic {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(defaultValue = "false") Boolean pinned,
                                       @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("CompilationControllerPublic.class getAll()");
        return service.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto findById(@PathVariable int compId) {
        log.info("CompilationController.class findById() id {}", compId);
        return service.findById(compId);
    }
}