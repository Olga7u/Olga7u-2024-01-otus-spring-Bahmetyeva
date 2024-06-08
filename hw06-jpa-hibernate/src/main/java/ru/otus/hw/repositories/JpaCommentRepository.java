package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public void deleteById(long id) {
        Comment comment = entityManager.find(Comment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
        } else {
            throw new EntityNotFoundException("Comment with id %d not found".formatted(id));
        }
    }
}
