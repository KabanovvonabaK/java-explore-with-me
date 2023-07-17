package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentForView;
import ru.practicum.comment.dto.CommentUserDto;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentService {
    CommentForView add(int userId, CommentUserDto inputCommentDto);

    CommentForView update(int comId, int userId, CommentUserDto inputCommentDto);

    CommentForView getCommentById(int userId, int comId);

    void deleteCommentByUser(int comId, int userId);

    void deleteCommentByAdmin(int comId);

    List<CommentForView> getCommentsForEvent(int eventId, int from, int size);

    Comment getCommentOrThrow(int comId);
}