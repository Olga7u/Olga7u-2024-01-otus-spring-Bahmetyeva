package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    public List<Comment> getBookComments(long bookId);

    public void insertComment(long bookId, String text);

    public void deleteById(long id);
}
