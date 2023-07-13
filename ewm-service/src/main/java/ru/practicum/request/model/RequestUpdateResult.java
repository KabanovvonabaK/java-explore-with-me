package ru.practicum.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class RequestUpdateResult{
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}