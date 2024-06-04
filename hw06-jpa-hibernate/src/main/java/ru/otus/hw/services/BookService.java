package ru.otus.hw.services;

import java.util.List;
import java.util.Set;

public interface BookService {
    String findById(long id);

    String findAll();

    String insert(String title, long authorId, Set<Long> genresIds);

    String update(long id, String title, long authorId, Set<Long> genresIds, List<String> commentTexts);

    void deleteById(long id);

    String insertComment(long bookId, String text);

    String findComments(long bookId);
}
