package ru.practicum.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommentUserDto {
    @PositiveOrZero(groups = {Create.class, Update.class}, message = "Event id is mandatory")
    private int eventId;
    @NotBlank(groups = {Create.class, Update.class}, message = "Comment should not be empty or blanked.")
    @Size(min = 1, max = 2000, groups = {Create.class, Update.class},
            message = "Comment should be from 1 up to 2000 length.")
    private String text;
}