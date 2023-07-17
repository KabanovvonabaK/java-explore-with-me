package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.utils.validation.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private final Integer id;
    @NotBlank(groups = Create.class, message = "User's name should not be empty or blanked.")
    @Size(min = 2, max = 250, groups = Create.class, message = "User's name should be from 2 up to 250 length.")
    private final String name;
    @Email(groups = Create.class, message = "User's email should have @ symbol and domain.")
    @NotBlank(groups = Create.class, message = "User's email should not be empty or blanked.")
    @Size(min = 6, max = 254, groups = Create.class, message = "User's email should be from 6 up to 254 length.")
    private final String email;
}