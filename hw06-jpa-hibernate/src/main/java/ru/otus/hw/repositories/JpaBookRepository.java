package ru.otus.hw.repositories;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    private final static String ERR_MESSAGE = "Book with id %d not found";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery(
                "select distinct b from Book b left join fetch b.author" +
                        " left join fetch b.genres" +
                        " left join fetch b.comments", Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }

        Book updatedBook = entityManager.find(Book.class, book.getId());
        if (updatedBook != null) {
            return entityManager.merge(book);
        } else {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(book.getId()));
        }
    }

    @Override
    public void deleteById(long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        } else {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(id));
        }
    }

    @Override
    public Book insertComment(long bookId, String text) {
        Book updatedBook = entityManager.find(Book.class, bookId);
        if (updatedBook == null) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(bookId));
        }
        List<Comment> comments = updatedBook.getComments();
        comments.add(new Comment(0, text));
        updatedBook.setComments(comments);

        return save(updatedBook);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        Book book = entityManager.find(Book.class, bookId);
        if (book == null) {
            throw new EntityNotFoundException(ERR_MESSAGE.formatted(bookId));
        }
        return book.getComments();
    }
}
