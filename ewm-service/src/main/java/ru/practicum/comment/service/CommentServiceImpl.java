package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentForView;
import ru.practicum.comment.dto.CommentUserDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.event.service.EventService;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.UnpublishedEventException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    @Transactional
    public CommentForView add(int userId, CommentUserDto inputCommentDto) {
        log.info("CommentServiceImpl.class add() userId {} with {}", userId, inputCommentDto);
        User userFromDb = userService.findUserById(userId);
        Event eventFromDb = eventService.findEventById(inputCommentDto.getEventId());

        if (eventFromDb.getStatus().equals(EventState.PUBLISHED)) {
            throw new UnpublishedEventException("Impossible to create comment to unpublished event");
        }

        Comment comment = CommentMapper.toComment(inputCommentDto, eventFromDb, userFromDb);
        comment.setCreatedOn(LocalDateTime.now());
        Comment result = commentRepository.save(comment);
        return CommentMapper.toView(result);
    }

    @Override
    public CommentForView getCommentById(int comId) {
        log.info("CommentServiceImpl.class getCommentById() comId {}", comId);
        Comment commentFromDb = getCommentOrThrow(comId);
        return CommentMapper.toView(commentFromDb);
    }

    @Override
    @Transactional
    public void deleteCommentByUser(int comId, int userId) {
        log.info("CommentServiceImpl.class deleteCommentByUser() userId {} comId {}", userId, comId);
        userService.findUserById(userId);
        Comment commentFromDb = getCommentOrThrow(comId);
        checkAuthorCommentOrThrow(userId, commentFromDb);
        commentRepository.deleteById(comId);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(int comId) {
        log.info("CommentServiceImpl.class deleteCommentByAdmin() comId {}", comId);
        commentRepository.findById(comId);
        commentRepository.deleteById(comId);
    }

    @Override
    @Transactional
    public CommentForView update(int comId, int userId, CommentUserDto inputCommentDto) {
        log.info("CommentServiceImpl.class update() userId {} comId {} with {}", userId, comId, inputCommentDto);
        userService.findUserById(userId);
        Comment commentFromDb = getCommentOrThrow(comId);
        checkAuthorCommentOrThrow(userId, commentFromDb);
        Comment comment = updateFieldsByUser(inputCommentDto, commentFromDb);
        Comment result = commentRepository.save(comment);
        return CommentMapper.toView(result);
    }

    @Override
    public List<CommentForView> getCommentsForEvent(int eventId, int from, int size) {
        log.info("CommentServiceImpl.class getCommentsForEvent() eventId {}", eventId);
        Pageable pageable = PageRequest.of(
                from == 0 ? 0 : (from / size), size);
        Event eventFromDb = eventService.findEventById(eventId);
        if (!eventFromDb.getStatus().equals(EventState.PUBLISHED)) {
            if (eventFromDb.getInitiator().getId().equals(eventId)) {
                List<Comment> comments = commentRepository.findAllByEvent_Id(eventId, pageable);
                return CommentMapper.toViewList(comments);
            } else {
                return Collections.emptyList();
            }
        }
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId, pageable);
        return CommentMapper.toViewList(comments);
    }

    @Override
    public Comment getCommentOrThrow(int comId) {
        return commentRepository.findById(comId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Comment with id %d not found", comId)));
    }

    private Comment updateFieldsByUser(CommentUserDto newComment, Comment oldComment) {
        return oldComment.toBuilder()
                .text(newComment.getText())
                .editedOn(LocalDateTime.now())
                .isEdited(true).build();
    }

    private void checkAuthorCommentOrThrow(int userId, Comment comment) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new ConflictException(String.format("User with id %d is not an author of comment with id %d",
                    userId, comment.getId()));
        }
    }
}