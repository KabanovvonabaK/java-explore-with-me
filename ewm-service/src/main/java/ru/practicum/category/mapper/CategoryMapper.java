package ru.practicum.category.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;

@UtilityClass
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toObject(CategoryDto dto) {
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}