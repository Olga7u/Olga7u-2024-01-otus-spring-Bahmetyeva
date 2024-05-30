package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findAllByBookId(Long bookId);

    Optional<Comment> findById(long id);

    Comment insert(long bookId, String text);

    void deleteById(long id);
}
