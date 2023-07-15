package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    private List<Integer> events;
    private Boolean pinned;
    @NotBlank(groups = {Create.class}, message = "Compilation title shouldn't be empty or blanked.")
    @Size(min = 1, max = 50, groups = {Create.class, Update.class},
            message = "Compilation title should be from 1 up to 50 length.")
    private final String title;
}