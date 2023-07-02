package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@Builder
public class EndpointHitDto {
    @NotEmpty
    private final String app;
    @NotEmpty
    private final String uri;
    @NotEmpty
    private String ip;
    @NotEmpty
    private final String timestamp;
}