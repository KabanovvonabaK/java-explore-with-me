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
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentUserController {
    private final CommentService commentService;

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentForView add(@Positive @PathVariable("userId") int userId,
                              @Validated(Create.class) @RequestBody CommentUserDto commentUserDto) {
        log.info("CommentUserController.class add() userId {} with {}", userId, commentUserDto);
        return commentService.add(userId, commentUserDto);
    }

    @GetMapping("/{comId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentForView findByIdForUser(@Positive @PathVariable("userId") int userId,
                                          @Positive @PathVariable("comId") int comId) {
        log.info("CommentUserController.class findByIdForUser() userId {} comId {}", userId, comId);
        return commentService.getCommentById(userId, comId);
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

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentForView> getAllCommentEvent(@PositiveOrZero @PathVariable int eventId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("CommentUserController.class getAllCommentEvent()");
        return commentService.getCommentsForEvent(eventId, from, size);
    }
}