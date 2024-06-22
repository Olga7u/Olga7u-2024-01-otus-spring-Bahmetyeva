package ru.otus.hw.services;

import java.util.List;

public interface BookService {
    String findById(String id);

    String findAll();

    String insert(String title, String authorName,  List<String> genreList);

    String update(String id, String title, String authorName,  List<String> genreList);

    void deleteById(String id);

    String findAllAuthors();

    String insertComment(String bookId, String text);
}
