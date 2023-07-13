package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.enums.RequestState;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UpdateRequestDto {
    @NotEmpty
    private final List<Integer> requestIds;
    @NotNull
    private final RequestState status;
}