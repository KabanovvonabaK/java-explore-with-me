package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryControllerAdmin {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Validated(Create.class) @RequestBody CategoryDto categoryDto) {
        log.info("CategoryControllerAdmin.class create() {}", categoryDto);
        return service.create(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@Validated(Update.class) @RequestBody CategoryDto categoryDto,
                              @PathVariable int catId) {
        log.info("CategoryControllerAdmin.class update() id {} with {}", catId, categoryDto);
        return service.update(categoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int catId) {
        log.info("CategoryControllerAdmin.class delete() id {}", catId);
        service.delete(catId);
    }
}