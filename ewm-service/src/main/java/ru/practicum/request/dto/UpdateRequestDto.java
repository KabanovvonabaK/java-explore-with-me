package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.RequestState;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UpdateRequestDto {
    @NotEmpty(groups = Update.class)
    private final List<Integer> requestIds;
    @NotNull(groups = Update.class)
    private final RequestState status;
}