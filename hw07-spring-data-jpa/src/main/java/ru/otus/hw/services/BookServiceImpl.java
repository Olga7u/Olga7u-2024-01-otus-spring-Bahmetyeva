package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final String ERR_MESSAGE = "Book with id %d not found";

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final CommentConverter commentConverter;

    @Override
    public String findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @Override
    public String findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Override
    @Transactional
    public String insert(String title, long authorId, Set<Long> genresIds) {
        Book book = save(0, title, authorId, genresIds, null);
        return bookConverter.bookToString(book);
    }

    @Override
    @Transactional
    public String update(long id, String title, long authorId, Set<Long> genresIds, List<String> commentTexts) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(id));
        }
        Book book = save(id, title, authorId, genresIds, commentTexts);
        return bookConverter.bookToString(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(id));
        }
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public String insertComment(long bookId, String text) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(bookId));
        }
        Book updatedBook = book.get();
        updatedBook.getComments().add(new Comment(0, text));
        bookRepository.save(updatedBook);

        return bookConverter.bookToString(updatedBook);
    }

    @Override
    public String findComments(long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(bookId));
        }
        List<Comment> comments = book.get().getComments();
        return comments.stream()
                .map(commentConverter::commentToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds, List<String> commentTexts) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));

        List<Genre> genreList = genreRepository.findAllById(genresIds);
        if (isEmpty(genreList) || genresIds.size() != genreList.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        Set<Genre> genres = new HashSet<>(genreList);

        List<Comment> comments = new ArrayList<>();
        if (!isEmpty(commentTexts)) {
            commentTexts.forEach(item -> comments.add(new Comment(0, item)));
        }

        var book = new Book(id, title, author, genres, comments);
        return bookRepository.save(book);
    }
}
