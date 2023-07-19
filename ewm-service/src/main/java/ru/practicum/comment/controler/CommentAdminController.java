package ru.practicum.comment.controler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PositiveOrZero @PathVariable int comId) {
        log.info("CommentAdminController.class delete() comId {}", comId);
        commentService.deleteCommentByAdmin(comId);
    }
}