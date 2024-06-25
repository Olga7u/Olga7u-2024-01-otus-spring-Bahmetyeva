package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"genres", "author"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"genres", "author"})
    Optional<Book> findById(long id);
}
