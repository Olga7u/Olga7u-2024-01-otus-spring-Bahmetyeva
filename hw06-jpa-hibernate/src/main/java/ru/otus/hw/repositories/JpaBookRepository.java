package ru.otus.hw.repositories;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = entityManager.createEntityGraph("book-graph");
        TypedQuery<Book> query = entityManager.createQuery(
                "select distinct b from Book b " +
                        "left join fetch b.author " +
                        "left join fetch b.genres ", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Book save(Book book) {

        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }

        Book updatedBook = entityManager.find(Book.class, book.getId());
        if (updatedBook != null) {
            return entityManager.merge(book);
        } else {
            throw new EntityNotFoundException("Book with id %d not found".formatted(book.getId()));
        }
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.remove(book);
        } else {
            throw new EntityNotFoundException("Book with id %d not found".formatted(id));
        }
    }
}
