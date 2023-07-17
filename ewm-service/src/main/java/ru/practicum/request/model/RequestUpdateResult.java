package ru.practicum.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RequestUpdateResult {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}