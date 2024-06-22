package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final String ERR_MESSAGE = "Book with id %s not found";

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    private final BookConverter bookConverter;

    @Override
    public String findById(String id) {
        Optional<Book> book = bookRepository.findById(id);

        return book.map(bookConverter::bookToStringWithComments)
                .orElse("Book with id %s not found".formatted(id));
    }

    @Override
    public String findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Override
    public String insert(String title, String authorName, List<String> genreList) {
        Book book = save(null, title, authorName, genreList);
        return bookConverter.bookToString(book);
    }

    @Override
    public String update(String id, String title, String authorName, List<String> genreList) {

        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(id));
        }
        Book book = save(id, title, authorName, genreList);
        return bookConverter.bookToString(book);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(id));
        }

        bookRepository.deleteById(id);
    }

    public String findAllAuthors() {
        Set<String> authors = bookRepository.findDistinctAuthorByFullName();
        return authors.stream()
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Transactional
    @Override
    public String insertComment(String bookId, String text) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(bookId));
        }
        Book updatedBook = book.get();

        Comment newComment = commentRepository.save(new Comment(text, updatedBook.getId()));

        List<Comment> newComments = updatedBook.getComments();
        if (newComments == null) {
            newComments = new ArrayList<>(List.of(newComment));
        } else {
            newComments.add(newComment);
        }

        updatedBook.setComments(newComments);
        bookRepository.save(updatedBook);

        return bookConverter.bookToStringWithComments(updatedBook);
    }

    private Book save(String id, String title, String authorName, List<String> genreList) {

        Author author = new Author(authorName);

        Set<Genre> genres = new HashSet<>();
        genreList.forEach(item -> genres.add(new Genre(item)));

        Book book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
