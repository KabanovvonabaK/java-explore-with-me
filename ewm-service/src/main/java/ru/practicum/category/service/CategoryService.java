package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto dto);

    CategoryDto update(CategoryDto dto, int id);

    void delete(int id);

    CategoryDto findById(int id);

    List<CategoryDto> getAll(Integer from, Integer size);
}