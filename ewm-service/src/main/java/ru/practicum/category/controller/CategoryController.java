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

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CategoryController {
    private final CategoryService service;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Validated(Create.class) @RequestBody CategoryDto categoryDto) {
        log.info("CategoryController.class create() {}", categoryDto);
        return service.create(categoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto update(@Validated(Update.class) @RequestBody CategoryDto categoryDto,
                              @PathVariable int catId) {
        log.info("CategoryController update() id {} with {}", catId, categoryDto);
        return service.update(categoryDto, catId);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int catId) {
        log.info("CategoryController.class delete() id {}", catId);
        service.delete(catId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAll(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                    @PositiveOrZero@RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("CategoryController.class getAll()");
        return service.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findById(@PathVariable int catId) {
        log.info("CategoryController.class findById() id {}", catId);
        return service.findById(catId);
    }
}