package ru.practicum.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.dto.CommentForView;
import ru.practicum.comment.dto.CommentUserDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public Comment toComment(CommentUserDto commentUserDto, Event eventMap, User userMap) {
        return Comment.builder()
                .id(0)
                .text(commentUserDto.getText())
                .user(userMap)
                .event(eventMap)
                .createdOn(LocalDateTime.now())
                .editedOn(LocalDateTime.now())
                .isEdited(false)
                .build();
    }

    public CommentForView toView(Comment comment) {
        return CommentForView.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .userId(comment.getUser().getId())
                .createdOn(comment.getCreatedOn())
                .editedOn(comment.getEditedOn())
                .build();
    }

    public List<CommentForView> toViewList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toView).collect(Collectors.toList());
    }
}