package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class CategoryDto {
    private final Integer id;
    @NotBlank(groups = {Create.class, Update.class}, message = "Category name shouldn't be empty or blanked.")
    @Size(min = 1, max = 50, groups = {Create.class, Update.class},
            message = "Category name should be from 1 up to 50 length.")
    private final String name;
}