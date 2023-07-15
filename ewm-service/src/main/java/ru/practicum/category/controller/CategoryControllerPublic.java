package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryControllerPublic {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getAll(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                    @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("CategoryControllerPublic.class getAll()");
        return service.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto findById(@PathVariable int catId) {
        log.info("CategoryControllerPublic.class findById() id {}", catId);
        return service.findById(catId);
    }
}