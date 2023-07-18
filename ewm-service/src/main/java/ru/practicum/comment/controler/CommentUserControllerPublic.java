package ru.practicum.comment.controler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentForView;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentUserControllerPublic {
    private final CommentService commentService;

    @GetMapping("/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentForView> getAllCommentEvent(@PositiveOrZero @PathVariable int eventId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                   @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("CommentUserController.class getAllCommentEvent()");
        return commentService.getCommentsForEvent(eventId, from, size);
    }

    @GetMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentForView findById(@Positive @PathVariable("comId") int comId) {
        log.info("CommentUserController.class findById() comId {}", comId);
        return commentService.getCommentById(comId);
    }
}