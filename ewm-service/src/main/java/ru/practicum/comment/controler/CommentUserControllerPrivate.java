package ru.practicum.comment.controler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentForView;
import ru.practicum.comment.dto.CommentUserDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.utils.validation.Create;
import ru.practicum.utils.validation.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentUserControllerPrivate {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentForView add(@Positive @RequestHeader("X-Explore-User-Id") int userId,
                              @Validated(Create.class) @RequestBody CommentUserDto commentUserDto) {
        log.info("CommentUserController.class add() userId {} with {}", userId, commentUserDto);
        return commentService.add(userId, commentUserDto);
    }

    @PatchMapping("/{comId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentForView update(@PositiveOrZero @PathVariable int comId,
                                 @PositiveOrZero @PathVariable int userId,
                                 @Validated(Update.class) @RequestBody CommentUserDto updateCommentDto) {
        log.info("CommentUserController.class update() userId {} commId {} with {}",
                userId, comId, updateCommentDto);
        return commentService.update(comId, userId, updateCommentDto);
    }

    @DeleteMapping("/{comId}/user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PositiveOrZero @PathVariable int comId,
                       @PositiveOrZero @PathVariable int userId) {
        log.info("CommentUserController.class delete() userId {} commId {}", userId, comId);
        commentService.deleteCommentByUser(comId, userId);
    }
}