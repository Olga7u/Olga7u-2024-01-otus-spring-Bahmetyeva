package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    private final CommentConverter commentConverter;

    @Transactional(readOnly = true)
    @Override
    public String findById(long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    @Transactional(readOnly = true)
    @Override
    public String findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @Transactional
    @Override
    public String insert(String title, long authorId, Set<Long> genresIds) {
        Book book = save(0, title, authorId, genresIds, null);
        return bookConverter.bookToString(book);
    }

    @Transactional
    @Override
    public String update(long id, String title, long authorId, Set<Long> genresIds, List<String> commentTexts) {
        Book book = save(id, title, authorId, genresIds, commentTexts);
        return bookConverter.bookToString(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    @Override
    public String insertComment(long bookId, String text) {
        Book book = bookRepository.insertComment(bookId, text);
        return bookConverter.bookToString(book);
    }

    @Transactional(readOnly = true)
    @Override
    public String findComments(long bookId) {
        List<Comment> comments = bookRepository.findByBookId(bookId);
        return comments.stream()
                .map(commentConverter::commentToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds, List<String> commentTexts) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));

        var genres = genreRepository.findAllByIds(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        List<Comment> comments = new ArrayList<>();
        if (!isEmpty(commentTexts)) {
            commentTexts.forEach(item -> comments.add(new Comment(0, item)));
        }

        var book = new Book(id, title, author, genres, comments);
        return bookRepository.save(book);
    }
}
