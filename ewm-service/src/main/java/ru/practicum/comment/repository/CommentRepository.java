package ru.practicum.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.dto.CommentEvent;
import ru.practicum.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select count(c) from Comment c where c.event.id = ?1")
    Long countCommentsForEvent(Long id);

    List<Comment> findAllByEvent_Id(int id, Pageable pageable);

    @Query("select new ru.practicum.comment.dto.CommentEvent(c.event.id, count(c))" +
            "from Comment c where c.event.id in ?1 group by c.event.id")
    List<CommentEvent> getCommentsEvents(List<Long> eventIds);
}